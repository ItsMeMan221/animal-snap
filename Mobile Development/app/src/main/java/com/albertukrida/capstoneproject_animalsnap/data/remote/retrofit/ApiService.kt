package com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit

import com.albertukrida.capstoneproject_animalsnap.data.remote.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("login")
    fun postLoginUser(
        @Body body: DataClassLoginRegister
    ): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun postRegisterUser(
        @Body body: DataClassLoginRegister
    ): Call<DataResponse>

    @POST("refresh_token")
    fun refreshToken(): Call<RefreshTokenResponse>

    @GET("/habitat")
    fun getAllHabitats(): Call<List<AllHabitatsResponseItem>>

    @GET("/animal")
    fun getAllAnimal(): Call<List<AllAnimalsResponseItem>>

    @GET("/animal/{uid}")
    fun getAnimalById(
        @Path("uid") uid: String
    ): Call<AnimalDetailResponse>
    
    @Multipart
    @POST("classify/{uid}")
    fun postClassify(
        @Path("uid") uid: String,
        @Part file: MultipartBody.Part
    ): Call<ClassifyResponse>

    @GET("/classify/{classify_id}")
    fun getClassifyResult(
        @Path("classify_id") id: String
    ): Call<AnimalDetailResponse>

    @GET("/classify/history/{uid}")
    suspend fun getHistory(
        @Path("uid") uid: String,
        @Query("page") page: Int
    ): HistoryResponse

    @GET("/classify/history/{uid}")
    fun getHist(
        @Path("uid") uid: String,
        @Query("page") page: Int
    ): Call<HistoryResponse>

    @GET("profile/{uid}")
    fun getProfile(
        @Path("uid") uid: String
    ): Call<ProfileResponse>

    @Multipart
    @POST("upload_picture/{uid}")
    fun updateProfPict(
        @Path("uid") uid: String,
        @Part file: MultipartBody.Part
    ): Call<DataResponse>

    @POST("upload_avatar/{uid}")
    fun updateAvatar(
        @Path("uid") uid: String,
        @Body body: DataClassAvatar
    ): Call<DataResponse>

    @PUT("change_name/{uid}")
    fun changeName(
        @Path("uid") uid: String,
        @Body data: DataClassChangeName
    ): Call<DataResponse>

    @PUT("change_pass/{uid}")
    fun changePassword(
        @Path("uid") uid: String,
        @Body data: DataClassChangePassword
    ): Call<DataResponse>
}