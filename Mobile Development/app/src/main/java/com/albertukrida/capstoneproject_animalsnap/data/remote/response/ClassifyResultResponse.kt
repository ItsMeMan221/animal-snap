package com.albertukrida.capstoneproject_animalsnap.data.remote.response

import com.google.gson.annotations.SerializedName

data class ClassifyResultResponse(

	@field:SerializedName("donasi")
	val donasi: List<Any>,

	@field:SerializedName("nama_hewan")
	val namaHewan: String,

	@field:SerializedName("habitat")
	val habitat: List<HabitatItem>,

	@field:SerializedName("gambar_hewan_user")
	val gambarHewanUser: String,

	@field:SerializedName("deskripsi_hewan")
	val deskripsiHewan: String,

	@field:SerializedName("status_hewan")
	val statusHewan: String,

	@field:SerializedName("nama_class")
	val namaClass: String,

	@field:SerializedName("deskripsi_class")
	val deskripsiClass: String,

	@field:SerializedName("gambar_hewan")
	val gambarHewan: String
)

data class HabitatItem(

	@field:SerializedName("gambar_habitat")
	val gambarHabitat: String,

	@field:SerializedName("nama_habitat")
	val namaHabitat: String,

	@field:SerializedName("deskripsi_habitat")
	val deskripsiHabitat: String
)
