import tensorflow as tf
import tensorflow_hub as hub

import numpy as np

from PIL import Image
from io import BytesIO
import os

from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required

from datetime import datetime

import secrets

from sqlalchemy.orm.exc import NoResultFound

from utils.connector import conn
from models.user_classify import UserClassify
from modules.bucket_user import bucket

from sqlalchemy import insert, select

import math

classify_bp = Blueprint('classify', __name__, url_prefix='/classify')

custom_objects = {"KerasLayer": hub.KerasLayer}

model = tf.keras.models.load_model("model.h5", custom_objects=custom_objects)


def norm_img(file):
    data = Image.open(file)
    data = data.resize((224, 224))
    data = np.array(data)
    data = data / 255.0
    data = np.expand_dims(data, axis=0)
    return data


def predict_animal(data):
    predictions = model.predict(data)
    label = np.argmax(predictions)
    return label


@classify_bp.route("/<uid>", methods=["POST"])
@jwt_required()
def classify(uid):
    files = request.files.get('picture')
    files_ext = os.path.splitext(files.filename)[1].lower()

    if files is None or files.filename == '':
        return jsonify({"status": "error", "message": "Upload gambar anda"}), 400

    if files_ext not in [".jpg", ".jpeg", ".png"]:
        return jsonify({"status": "error", "message": "format gambar harus jpg atau png atau jpeg"}), 400

    try:
        image_to_predict = norm_img(files)
        data = predict_animal(image_to_predict)

        # Value to be stored
        classified_id = secrets.token_hex(12)
        animal_id = int(data)
        date_classified = datetime.now()
        date_classified = date_classified.strftime("%d-%m-%Y")

        # Upload image to bucket
        image = Image.open(files)
        resize_image = image.resize((400, 400))

        with BytesIO() as output:
            resize_image.save(output, format=image.format)
            image_bytes = output.getvalue()

        filename = secrets.token_hex(6) + files_ext
        files_ref = bucket.blob('user_classify/' + filename)
        files.seek(0)
        files_ref.upload_from_file(
            BytesIO(image_bytes), content_type=f"image/{image.format}")
        public_url = files_ref.public_url

        # Insert to database
        conn.session.execute(insert(UserClassify).values(
            id=classified_id,
            user_id=uid,
            animal_id=animal_id,
            date_classified=str(date_classified),
            animal_image=public_url
        ))
        conn.session.commit()
        return jsonify({"status": "OK", "id": classified_id, "animal_predict": animal_id}), 201

    except Exception as e:
        conn.session.rollback()
        return jsonify({"status": "error", "message": f"Internal error : {e}"}), 500
    finally:
        conn.session.close()


@classify_bp.route("/<id_classify>", methods=["GET"])
@jwt_required()
def get_classify_result(id_classify):
    try:
        # Getting the master value
        user_classify = conn.session.scalars(
            select(UserClassify).where(UserClassify.id == id_classify)).one()

        # Get the animal id
        animal_id = user_classify.animal_id

        # Get the animal from animal table
        animal_query = conn.text(
            "SELECT a.nama nama_animal ,a.deskripsi deskripsi_animal, a.donasi_id, a.habitat_id, a.animal_picture, c.nama nama_class, c.deskripsi class_deskripsi, s.nama status_animal FROM animals a LEFT JOIN class c ON c.id = a.class_id LEFT JOIN status s ON s.id = a.status_id WHERE a.id = :animal_id"
        )
        animal = conn.session.execute(
            animal_query, {"animal_id": animal_id}).fetchone()

        # Value that might be multiple
        res_donasi = []
        res_habitat = []
        donasi_id = animal.donasi_id
        habitat_id = animal.habitat_id

        if donasi_id is not None and donasi_id != "":
            # String donasi to array
            donasi_id = donasi_id.split(",")
            # query each of id
            for i in donasi_id:
                query_donasi = conn.text(
                    "SELECT nama, link FROM donasi WHERE id = :id_donasi")
                donasi = conn.session.execute(
                    query_donasi, {"id_donasi": i}).fetchone()
                temp = {"nama_organisasi": donasi.nama,
                        "link_donasi": donasi.link}
                res_donasi.append(temp)

        if habitat_id is not None and habitat_id != "":
            # String habitat to array
            habitat_id = habitat_id.split(",")
            # query each of id
            for i in habitat_id:
                query_habitat = conn.text(
                    "SELECT nama, gambar, deskripsi FROM habitat WHERE id = :id_habitat")
                habitat = conn.session.execute(
                    query_habitat, {"id_habitat": i}).fetchone()
                temp = {"nama_habitat": habitat.nama, "gambar_habitat": habitat.gambar,
                        "deskripsi_habitat": habitat.deskripsi}
                res_habitat.append(temp)

        # Concat to final response
        response = {
            "nama_hewan": animal.nama_animal,
            "deskripsi_hewan": animal.deskripsi_animal,
            "nama_class": animal.nama_class,
            "deskripsi_class": animal.class_deskripsi,
            "status_hewan": animal.status_animal,
            "gambar_hewan": animal.animal_picture,
            "gambar_hewan_user": user_classify.animal_image,
            "donasi": res_donasi,
            "habitat": res_habitat
        }
        return jsonify(response), 200
    except NoResultFound as e:
        return jsonify({"status": "error", "message": "Hewan tidak ada!"}), 204
    except Exception as e:
        return jsonify({"status": "error", "message": f"Internal error : {e}"}), 500
    finally:
        conn.session.close()


# History Classify
@classify_bp.route("/history/<uid>", methods=["GET"])
@jwt_required()
def history_classify(uid):
    page = int(request.args.get("page", 1))
    limit = 5
    # Getting the master value
    try:
        histories_query = conn.text(
            "SELECT UC.id, UC.date_classified, UC.animal_image, A.nama nama_hewan, S.nama status_hewan FROM user_classify UC LEFT JOIN animals A ON A.id = UC.animal_id LEFT JOIN status S ON S.id = A.status_id WHERE UC.user_id = :uid")
        histories = conn.session.execute(
            histories_query, {"uid": uid}).fetchall()
        # Begin pagination
        total_rec = len(histories)
        total_page = math.ceil(total_rec/limit)
        offset_value = (page - 1) * limit

        paginated_historties = histories[offset_value:offset_value + limit]

        response = [
            {
                "id_classification": history.id,
                "gambar_hewan": history.animal_image,
                "tanggal_klasifikasi": history.date_classified,
                "nama_hewan": history.nama_hewan,
                "status_hewan": history.status_hewan
            }
            for history in paginated_historties]
        if response == []:
            return jsonify({"status": "OK", "message": 'Tidak ada history klasifikasi'}), 204

        return jsonify({
            "total_records": total_rec,
            "total_page": total_page,
            "current_page": page,
            "data": response
        }), 200

    except Exception as e:
        return jsonify({"status": "error", "message": f"Internal error : {e}"}), 500
    finally:
        conn.session.close()
