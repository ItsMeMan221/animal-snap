from utils.connector import conn
from .users import Users


class UserClassify(conn.Model):
    __tablename__ = "user_classify"

    id = conn.Column(conn.Integer, primary_key=True)
    user_id = conn.Column(conn.String(255), conn.ForeignKey('users.uid'))
    user_id = conn.Column(conn.String(255), conn.ForeignKey('animals.id'))
    date_classified = conn.Column(conn.DateTime())
    animals_image = conn.Column(conn.String(255), nullable=False)

    user = conn.relationship("Users", back_populates='user_classify')
    animal = conn.relationship("Animals")
