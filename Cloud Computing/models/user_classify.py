from utils.connector import conn
from .users import Users
from .animals import Animals


class UserClassify(conn.Model):
    __tablename__ = "user_classify"

    id = conn.Column(conn.String(255), primary_key=True)
    user_id = conn.Column(conn.String(255))
    animal_id = conn.Column(conn.Integer)
    date_classified = conn.Column(conn.String(255))
    animal_image = conn.Column(conn.String(255), nullable=False)
