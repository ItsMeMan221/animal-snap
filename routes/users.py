from flask import Blueprint, jsonify, request
import secrets
import os

from sqlalchemy import insert, select, update
from werkzeug.security import generate_password_hash, check_password_hash
from sqlalchemy.orm.exc import NoResultFound
from flask_jwt_extended import create_access_token, create_refresh_token, get_jwt_identity, jwt_required

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

    # Nama validation
    if "nama" not in forms:
        return jsonify({"status": "error", "message": "Mohon isi field nama"}), 400
    if len(forms["nama"]) < 4:
        return jsonify({"status": "error", "message": "Nama minimal 8 karakter"}), 400

    # Register User
    try:
        conn.session.execute(insert(Users).values(
            uid=uid,
            nama=forms["nama"],
            email=forms["email"],
            password=generate_password_hash(forms["password"]),
        ))
        conn.session.commit()
        return jsonify({"status": "OK", "message": f"User {forms['email']} telah berhasil teregristrasi"}), 201
    except Exception as e:
        conn.session.rollback()
        return jsonify({"status": "error", "message": f"Sepertinya ada error pada sisi kami, err: {e}"}), 500


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
        }), 200
    except NoResultFound:
        return jsonify({"status": "error", "message": "Credential belum pernah terdaftar"}), 401
    except Exception as e:
        return jsonify({"status": "Internal error", "message": f"Sepertinya ada error pada sisi kami, err: {e}"}), 500


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
    if prev_pict.profile_picture is not None:
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
