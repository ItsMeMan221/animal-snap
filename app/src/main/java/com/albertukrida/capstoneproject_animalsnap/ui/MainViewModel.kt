package com.albertukrida.capstoneproject_animalsnap.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.albertukrida.capstoneproject_animalsnap.data.HistoryRepository
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.DataItem
import com.albertukrida.capstoneproject_animalsnap.di.Injection

class MainViewModel(historyRepository: HistoryRepository) : ViewModel() {

    val history: LiveData<PagingData<DataItem>> =
        historyRepository.getHistory().cachedIn(viewModelScope)
}

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}