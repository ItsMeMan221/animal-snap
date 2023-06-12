package com.albertukrida.capstoneproject_animalsnap.data.remote.response

import com.google.gson.annotations.SerializedName

data class AllAnimalsResponseItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("animal_id")
	val animalId: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("gambar")
	val gambar: String
)
