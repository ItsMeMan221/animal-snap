from utils.connector import conn


class Class(conn.Model):
    __tablename__ = "class"
    id = conn.Column(conn.Integer, primary_key=True)
    name = conn.Column(conn.String(255))
    deskripsi = conn.Column(conn.Text)
