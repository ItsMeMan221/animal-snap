package com.albertukrida.capstoneproject_animalsnap.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.DataItem
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiService

class HistoryRepository(private val apiService: ApiService) {
    fun getHistory(): LiveData<PagingData<DataItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                HistoryPagingSource(apiService)
            }
        ).liveData
    }
}