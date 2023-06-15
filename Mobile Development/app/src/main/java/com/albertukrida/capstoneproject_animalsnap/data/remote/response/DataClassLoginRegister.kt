package com.albertukrida.capstoneproject_animalsnap.data.remote.response

data class DataClassLoginRegister(
	val email: String,
	val password: String,
	val re_pass: String ?= null,
	val nama: String ?= null
)