
from flask import Blueprint, jsonify

from flask_jwt_extended import jwt_required
from sqlalchemy import select
from sqlalchemy.orm.exc import NoResultFound

from models.animals import Animals

from utils.connector import conn

animal_bp = Blueprint('animal', __name__, url_prefix="/animal")


@animal_bp.route("", methods=["GET"])
@jwt_required()
def get_all_animals():
    try:
        items = conn.session.scalars(select(Animals)).all()
        return jsonify(
            [{
                "animal_id": item.id,
                "nama": item.nama,
                "deskripsi": item.deskripsi,
                "gambar": item.animal_picture,
            } for item in items]
        ), 200
    except Exception as e:
        return jsonify({"status": "error", "message": f"Sepertinya ada kesalahan dari kami {e}"}), 500
    finally:
        conn.session.close()


@animal_bp.route("/<id>", methods=["GET"])
@jwt_required()
def get_animal_by_id(id):
    try:
        # Get the animal from animal table
        animal_query = conn.text(
            "SELECT a.nama nama_animal ,a.deskripsi deskripsi_animal, a.donasi_id, a.habitat_id, a.animal_picture, c.nama nama_class, c.deskripsi class_deskripsi, s.nama status_animal FROM animals a LEFT JOIN class c ON c.id = a.class_id LEFT JOIN status s ON s.id = a.status_id WHERE a.id = :animal_id"
        )
        animal = conn.session.execute(
            animal_query, {"animal_id": id}).fetchone()

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
            "donasi": res_donasi,
            "habitat": res_habitat
        }
        return jsonify(response), 200
    except NoResultFound as e:
        return jsonify({"status": "error", "message": "Hewan tidak ada!"}), 400
    except Exception as e:
        return jsonify({"status": "error", "message": f"Internal error : {e}"}), 500
    finally:
        conn.session.close()
