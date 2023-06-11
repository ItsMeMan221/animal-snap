package com.albertukrida.capstoneproject_animalsnap.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("total_records")
	val totalRecords: Int,

	@field:SerializedName("total_page")
	val totalPage: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
)

data class DataItem(

	@field:SerializedName("nama_hewan")
	val namaHewan: String,

	@field:SerializedName("id_classification")
	val idClassification: String,

	@field:SerializedName("tanggal_klasifikasi")
	val tanggalKlasifikasi: String,

	@field:SerializedName("status_hewan")
	val statusHewan: String,

	@field:SerializedName("gambar_hewan")
	val gambarHewan: String
)
