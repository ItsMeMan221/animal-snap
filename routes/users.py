from flask import Blueprint, jsonify, request
import secrets
import os

from sqlalchemy import insert, select, update
from werkzeug.security import generate_password_hash, check_password_hash
from sqlalchemy.orm.exc import NoResultFound
from flask_jwt_extended import create_access_token, create_refresh_token, get_jwt_identity, jwt_required
from datetime import datetime


from utils.connector import conn
from models.users import Users
from modules.bucket_user import bucket

user_bp = Blueprint('user', __name__)


@user_bp.route("/register", methods=["POST"])
def register_user():
    forms = request.json
    uid = secrets.token_hex(8)

    # Validation Email
    if "email" not in forms:
        return jsonify({"status": "error", "message": "Mohon isi field email"}), 400
    check_ext_email = conn.session.scalars(
        select(Users).where(Users.email == forms["email"])).one_or_none()
    if check_ext_email:
        return jsonify({"status": "error", "message": "Email telah digunakan"}), 400

    # Password Validation
    if "password" not in forms:
        return jsonify({"status": "error", "message": "Mohon isi field password"}), 400
    if len(forms["password"]) < 8:
        return jsonify({"status": "error", "message": "Password minimal 8 karakter"}), 400

    if "re_pass" not in forms:
        return jsonify({"status": "error", "message": "Isi field ulangi password! "}), 400
    if forms["password"] != forms["re_pass"]:
        return jsonify({"status": "error", "message": "Nilai kedua password harus sama"}), 400

    # Nama validation
    if "nama" not in forms:
        return jsonify({"status": "error", "message": "Mohon isi field nama"}), 400
    if len(forms["nama"]) < 4:
        return jsonify({"status": "error", "message": "Nama minimal 8 karakter"}), 400

    # Register User
    try:
        date_joined = datetime.now()
        date_joined = date_joined.strftime("%d-%m-%Y")
        conn.session.execute(insert(Users).values(
            uid=uid,
            nama=forms["nama"],
            email=forms["email"],
            password=generate_password_hash(forms["password"]),
            date_joined=date_joined
        ))
        conn.session.commit()
        return jsonify({"status": "OK", "message": f"User {forms['email']} telah berhasil teregristrasi"}), 201
    except Exception as e:
        conn.session.rollback()
        return jsonify({"status": "error", "message": f"Sepertinya ada error pada sisi kami, err: {e}"}), 500
    finally:
        conn.session.close()


@user_bp.route("/login", methods=["POST"])
def login():
    forms = request.json

    # Validation
    if "email" not in forms:
        return jsonify({"status": "error", "message": "Mohon isi field email"}), 400
    if "password" not in forms:
        return jsonify({"status": "error", "message": "Mohon isi field password"}), 400
    try:
        user = conn.session.scalars(select(Users).where(
            Users.email == forms["email"])).one()
        # Check password
        if not check_password_hash(user.password, forms["password"]):
            return jsonify({"status": "error", "message": "Credential anda salah"}), 401
        # Create token
        token = create_access_token(
            identity={'uid': user.uid, 'email': user.email})
        refresh_token = create_refresh_token(
            identity={'uid': user.uid, 'email': user.email})
        return jsonify({
            'token': token,
            'refresh_token': refresh_token,
            'uid': user.uid,
            'email': user.email
        }), 200
    except NoResultFound:
        return jsonify({"status": "error", "message": "Credential belum pernah terdaftar"}), 401
    except Exception as e:
        return jsonify({"status": "Internal error", "message": f"Sepertinya ada error pada sisi kami, err: {e}"}), 500
    finally:
        conn.session.close()


@user_bp.route('/refresh_token', methods=["POST"])
@jwt_required(refresh=True)
def refresh_token():
    curr_user = get_jwt_identity()
    token = create_access_token(identity=curr_user)
    return jsonify({'token': token}), 201


@user_bp.route('/session', methods=["GET"])
@jwt_required()
def get_current_user():
    curr_user = get_jwt_identity()
    return jsonify({'uid': curr_user['uid'], 'email': curr_user['email']}), 200


@user_bp.route('/upload_picture/<uid>', methods=["POST"])
@jwt_required()
def upload_user_pict(uid):
    form = request.files["profile_picture"]
    image_ext = os.path.splitext(form.filename)[1]
    if image_ext not in [".jpg", ".jpeg", ".png"]:
        return jsonify({"status": "error", "message": "format gambar harus jpg atau png atau jpeg"})
    prev_pict = conn.session.scalars(
        select(Users).where(Users.uid == uid)).one()

    # If there is Profile Picture
    if prev_pict.profile_picture is not None:

        # Splitting the path of an image
        prev_pict_list = prev_pict.profile_picture.split("/")
        if len(prev_pict_list) > 2:
            prev_pict_hoster = prev_pict_list[2]
            # Profile picture is in bucket
            if prev_pict_hoster == 'storage.googleapis.com':
                prev_pict_name = prev_pict.profile_picture.split("/")[-1]
                file_ref = bucket.blob('profile_pict/' + prev_pict_name)
                file_ref.delete()

    form.filename = secrets.token_hex(4) + image_ext
    image_ref = bucket.blob('profile_pict/' + form.filename)
    image_ref.upload_from_file(form, content_type="image")
    public_url = image_ref.public_url
    try:
        conn.session.execute(update(Users).where(Users.uid == uid).values(
            profile_picture=public_url
        ))
        conn.session.commit()
        return jsonify({"status": "OK", "message": "profile picture telah diperbaharui"}), 200
    except Exception as e:
        conn.session.rollback()
        return jsonify({"status": "error", "message": f"Sepertinya ada error pada sisi kami, err: {e}"}), 500


@user_bp.route('/delete_profile_picture/<uid>', methods=["DELETE"])
@jwt_required()
def delete_profile_picture(uid):
    try:
        check_pict = conn.session.scalars(
            select(Users).where(Users.uid == uid)).one()
        if check_pict.profile_picture is not None:
            check_pict_list = check_pict.profile_picture.split("/")
            if len(check_pict_list) > 2:
                check_pict_hoster = check_pict_list[2]
                if check_pict_hoster == 'storage.googleapis.com':
                    pict_name = check_pict.profile_picture.split("/")[-1]
                    file_ref = bucket.blob('profile_pict/' + pict_name)
                    file_ref.delete()
            conn.session.execute(update(Users).where(
                Users.uid == uid).values(profile_picture=None))
            conn.session.commit()
            return jsonify({"status": "OK", "message": "profile picture telah dihapus"}), 200
    except NoResultFound:
        conn.session.rollback()
        return jsonify({"status": "error", "message": "User ini tidak ada"}), 404
    except Exception as e:
        conn.session.rollback()
        return jsonify({"status": "error", "message": f"Sepertinya ada error pada sisi kami, err: {e}"}), 500
    finally:
        conn.session.close()


@user_bp.route('/upload_avatar/<uid>', methods=["POST"])
@jwt_required()
def upload_avatar(uid):
    try:
        prev_pict = conn.session.scalars(
            select(Users).where(Users.uid == uid)).one()
        image_path = request.json
        if "image_path" not in image_path:
            return jsonify({"status": "error", "message": "image path tidak ditemukan"}), 400
            # Check Prev Picture
        if prev_pict.profile_picture is not None:
            prev_pict_hoster = prev_pict.profile_picture.split("/")[2]
            if prev_pict_hoster == "storage.googleapis.com":
                pict_name = prev_pict.profile_picture.split("/")[-1]
                file_ref = bucket.blob('profile_pict/' + pict_name)
                file_ref.delete()
        conn.session.execute(update(Users).where(
            Users.uid == uid).values(profile_picture=image_path["image_path"]))
        conn.session.commit()
        return jsonify({"status": "OK", "message": "profile picture telah diperbaharui"})
    except Exception as e:
        conn.session.rollback()
        return jsonify({"status": "error", "message": f"Sepertinya ada error pada sisi kami, err: {e}"}), 500
    finally:
        conn.session.close()


@user_bp.route("/profile/<uid>", methods=["GET"])
@jwt_required()
def get_profile(uid):
    try:
        user = conn.session.scalars(
            select(Users).where(Users.uid == uid)).one()
        return jsonify({"email": user.email, "nama": user.nama, "profile_picture": user.profile_picture, "date_joined": user.date_joined}), 200
    except NoResultFound:
        return jsonify({"status": "error", "message": "Gagal mengambil profile anda"}), 400
    except Exception as e:
        return jsonify({"status": "error", "message": f"Internal error: {e}"}), 500
    finally:
        conn.session.close()


@user_bp.route("/change_pass/<uid>", methods=["PUT"])
@jwt_required()
def change_pass(uid):
    forms = request.json
    user = conn.session.scalars(
        select(Users).where(Users.uid == uid)).one()

    # Old Password Validation
    if "password" not in forms:
        return jsonify({"status": "error", "message": "Mohon isi field password"}), 400
    if not check_password_hash(user.password, forms["password"]):
        return jsonify({"status": "error", "message": "Credential anda salah"}), 401

    # New Password Validation
    if "new_pass" not in forms:
        return jsonify({"status": "error", "message": "Mohon isi field password baru"}), 400
    if len(forms["new_pass"]) < 8:
        return jsonify({"status": "error", "message": "Password minimal 8 karakter"}), 400

    # Retype Password Validation
    if forms["re_pass"] != forms["new_pass"]:
        return jsonify({"status": "error", "message": "Mohon isi field sesuai dengan password baru"}), 400

    try:
        new_pass = generate_password_hash(forms["new_pass"])
        conn.session.execute(
            update(Users).where(Users.uid == uid).values(password=new_pass)
        )
        conn.session.commit()
        return jsonify({"status": "OK", "message": "Password telah diubah"}), 200
    except Exception as e:
        conn.session.rollback()
        return jsonify({"status": "error", "message": f"Sepertinya ada error pada sisi kami, err: {e}"}), 500
    finally:
        conn.session.close()


@user_bp.route("/change_name/<uid>", methods=["PUT"])
@jwt_required()
def change_name(uid):
    forms = request.json
    if "name" not in forms:
        return jsonify({"status": "error", "message": "Mohon isi field name"}), 400
    try:
        conn.session.execute(update(Users).where(
            Users.uid == uid).values(nama=forms["name"]))
        conn.session.commit()
        return jsonify({"status": "OK", "message": "Nama telah diubah"}), 200
    except Exception as e:
        conn.session.rollback()
        return jsonify({"status": "error", "message": f"Sepertinya ada error pada sisi kami, err: {e}"}), 500
    finally:
        conn.session.close()
