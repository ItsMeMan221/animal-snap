from utils.connector import conn


class Animals(conn.Model):
    __tablename__ = 'animals'

    id = conn.Column(conn.Integer, primary_key=True)
    nama = conn.Column(conn.String(255))
    deskripsi = conn.Column(conn.String(255))
    animal_picture = conn.Column(conn.String(255))
    habitat_id = conn.Column(conn.Integer, conn.ForeignKey('habitats.id'))
    donasi_id = conn.Column(conn.Integer, conn.ForeignKey('donasi.id'))
    class_id = conn.Column(conn.Integer, conn.ForeignKey('class.id'))
    habitat = conn.relationship("Habitat")
    donasi = conn.relationship("Donasi")
    class_ = conn.relationship("Class")
