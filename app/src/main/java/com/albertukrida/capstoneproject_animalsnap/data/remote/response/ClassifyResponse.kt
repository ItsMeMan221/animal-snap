package com.albertukrida.capstoneproject_animalsnap.data.remote.response

import com.google.gson.annotations.SerializedName

data class ClassifyResponse(

	@field:SerializedName("animal_predict")
	val animalPredict: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("status")
	val status: String
)
