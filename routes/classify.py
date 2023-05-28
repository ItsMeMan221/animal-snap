import tensorflow as tf
from tensorflow import keras
import numpy as np

import io
from PIL import Image
import os

from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required

classify_bp = Blueprint('classify', __name__, url_prefix='/classify')
model = keras.models.load_model("model.h5")


def norm_img(file):
    data = np.asarray(file)
    data = data / 255.0
    data = np.expand_dims(data, axis=-1)
    data = tf.image.resize(data, [299, 299])
    data = tf.concat([data, data, data], axis=-1)
    data = tf.expand_dims(data, axis=0)
    return data


def predict(x):
    predictions = model(x)
    predictions = tf.nn.softmax(predictions)
    pred0 = predictions[0]
    label = np.argmax(pred0)
    return label


@classify_bp.route("/<uid>", methods=["POST"])
@jwt_required()
def classify(uid):
    files = request.files.get('picture')
    files_ext = os.path.splitext(files.filename)[1]

    if files is None or files.filename == '':
        return jsonify({"status": "error", "message": "Upload gambar anda"}), 400

    if files_ext not in [".jpg", ".jpeg", ".png"]:
        return jsonify({"status": "error", "message": "format gambar harus jpg atau png atau jpeg"}), 400

    try:
        image_bytes = files.read()
        pillow_img = Image.open(io.BytesIO(image_bytes)).convert('L')
        tensor = norm_img(pillow_img)
        prediction = predict(tensor)
        data = {"pred": int(prediction)}

        return jsonify(data)

    except Exception as e:
        return jsonify({"status": "error", "message": f"Internal error : {e}"}), 500
