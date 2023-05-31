from utils.connector import conn


class Animals(conn.Model):
    __tablename__ = 'animals'

    id = conn.Column(conn.Integer, primary_key=True)
    nama = conn.Column(conn.String(255))
    deskripsi = conn.Column(conn.String(255))
    animal_picture = conn.Column(conn.String(255))
    habitat_id = conn.Column(conn.String(255))
    donasi_id = conn.Column(conn.String(255))
    class_id = conn.Column(conn.Integer)
    status_id = conn.Column(conn.Integer)
