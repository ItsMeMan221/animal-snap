
from flask import Blueprint, jsonify

from flask_jwt_extended import jwt_required
from sqlalchemy import select
from sqlalchemy.orm.exc import NoResultFound

from models.habitat import Habitat

from utils.connector import conn

habitat_bp = Blueprint('habitat', __name__, url_prefix="/habitat")


@habitat_bp.route("", methods=["GET"])
@jwt_required()
def get_all_habitat():
    try:
        items = conn.session.scalars(select(Habitat)).all()
        return jsonify(
            [{
                "id": item.id,
                "nama": item.nama,
                "deskripsi": item.deskripsi,
                "gambar": item.gambar,
            } for item in items]
        ), 200
    except Exception as e:
        return jsonify({"status": "error", "message": f"Sepertinya ada kesalahan dari kami {e}"}), 500
    finally:
        conn.session.close()


@habitat_bp.route("/<id>", methods=["GET"])
@jwt_required()
def get_habitat_by_id(id):
    try:
        item = conn.session.scalars(
            select(Habitat).where(Habitat.id == id)).one()
        return jsonify({
            "nama": item.nama,
            "deskripsi": item.deskripsi,
            "gambar": item.gambar,
        }), 200
    except NoResultFound as e:
        return jsonify({"status": "error", "message": "Habitat tidak ada!"}), 400
    except Exception as e:
        return jsonify({"status": "error", "message": f"Sepertinya ada kesalahan dari kami {e}"}), 500
    finally:
        conn.session.close()
