from utils.connector import conn


class Donasi(conn.Model):
    __tablename__ = 'donasi'

    id = conn.Column(conn.Integer, primary_key=True)
    nama = conn.Column(conn.String(255))
    link = conn.Column(conn.String(255))
