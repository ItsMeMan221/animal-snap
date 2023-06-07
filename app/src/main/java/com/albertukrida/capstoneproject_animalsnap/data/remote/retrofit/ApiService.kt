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

    @DELETE("delete_profile_picture/{uid}")
    fun deleteProfPict(
        @Path("uid") uid: String
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