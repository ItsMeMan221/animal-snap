from utils.connector import conn


class Users(conn.Model):

    __tablename__ = "users"

    id = conn.Column(conn.Integer, primary_key=True)
    uid = conn.Column(conn.String(225), unique=True, nullable=False)
    nama = conn.Column(conn.String(255), nullable=False)
    email = conn.Column(conn.String(255), unique=True, nullable=False)
    password = conn.Column(conn.String(255), unique=True, nullable=False)
    profile_picture = conn.Column(conn.String(255))
    # user_classify = conn.relationship('UserClassify', back_populates='user')
