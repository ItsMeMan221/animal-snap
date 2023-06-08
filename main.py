from flask import Flask, Response
from datetime import timedelta
import yaml
from flask_cors import CORS
from flask_swagger_ui import get_swaggerui_blueprint


from utils.connector import conn
from utils.token_jwt import jwt

from routes.errors import error_bp
from routes.users import user_bp
from routes.classify import classify_bp
from routes.habitat import habitat_bp
from routes.animal import animal_bp
app = Flask(__name__)

with open('app.yaml') as yaml_file:
    config_data = yaml.safe_load(yaml_file)

for key, value in config_data.get('env_variables', {}).items():
    app.config[key] = value

app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(hours=12)
app.config["JWT_REFRESH_TOKEN_EXPIRES"] = timedelta(days=15)


# swagger
SWAGGER_URL = '/api/docs'

API_URL = '/static/openapi.json'


swaggerui_blueprint = get_swaggerui_blueprint(

    SWAGGER_URL,
    API_URL,
    config={
        'app_name': "AnimalSnap API"
    },
)

app.register_blueprint(swaggerui_blueprint)


CORS(app)
conn.app = app
conn.init_app(app)
jwt.init_app(app)

app.register_blueprint(error_bp)
app.register_blueprint(user_bp)
app.register_blueprint(classify_bp)
app.register_blueprint(habitat_bp)
app.register_blueprint(animal_bp)


# @app.after_request
# def after_req(response: Response):
#     response.headers["Content-type"] = "application/json; charset=utf-8"
#     return response


if (__name__ == "__main__"):
    app.run(host="127.0.0.1", debug=True, port=5031)
