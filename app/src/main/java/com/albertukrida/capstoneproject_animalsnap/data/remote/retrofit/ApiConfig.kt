package com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit

import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.userModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiConfig {
    companion object{
        private const val baseUrl = "https://animal-snap-ftc4765tsq-et.a.run.app/"
        fun getApiService(token: String? = userModel.token): ApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(request)
                }
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}