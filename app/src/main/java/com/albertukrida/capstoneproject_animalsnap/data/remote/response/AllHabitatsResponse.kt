package com.albertukrida.capstoneproject_animalsnap.data.remote.response

import com.google.gson.annotations.SerializedName

data class AllHabitatsResponseItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("gambar")
	val gambar: String
)
