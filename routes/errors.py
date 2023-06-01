import traceback

from flask import Blueprint, jsonify
from werkzeug.exceptions import NotFound

error_bp = Blueprint("errors", __name__)


@error_bp.app_errorhandler(NotFound)
def handle_not_found(error):
    print(traceback.format_exc())
    return jsonify({"message": "URL ini tidak ada"}), 404


@error_bp.app_errorhandler(Exception)
def handle_generic_exception(error):
    print(traceback.format_exc())
    return jsonify({"message": f"Sepertinya ada kesalahan di bagian kami {error}"}), 500
