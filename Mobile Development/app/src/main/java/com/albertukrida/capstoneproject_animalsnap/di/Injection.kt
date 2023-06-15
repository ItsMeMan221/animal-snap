package com.albertukrida.capstoneproject_animalsnap.di

import com.albertukrida.capstoneproject_animalsnap.data.HistoryRepository
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): HistoryRepository {
        val apiService = ApiConfig.getApiService()
        return HistoryRepository(apiService)
    }
}