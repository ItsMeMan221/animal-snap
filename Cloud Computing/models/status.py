from utils.connector import conn


class Status(conn.Model):
    __tablename__ = 'status'

    id = conn.Column(conn.Integer, primary_key=True)
    nama = conn.Column(conn.String(255))
