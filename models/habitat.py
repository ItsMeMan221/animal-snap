from utils.connector import conn


class Habitat(conn.Model):
    __tablename__ = 'habitats'

    id = conn.Column(conn.Integer, primary_key=True)
    nama = conn.Column(conn.String(255))
    deskripsi = conn.Column(conn.Text)
