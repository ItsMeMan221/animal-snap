package com.albertukrida.capstoneproject_animalsnap.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = "profile_icon1.png",

	@field:SerializedName("date_joined")
	val dateJoined: String,

	@field:SerializedName("email")
	val email: String
)
