package com.albertukrida.capstoneproject_animalsnap.data.remote.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("token")
	val token: String
)
