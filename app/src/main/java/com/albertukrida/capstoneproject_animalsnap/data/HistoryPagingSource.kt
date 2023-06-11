package com.albertukrida.capstoneproject_animalsnap.data

import android.widget.Toast
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.DataItem
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiService
import com.albertukrida.capstoneproject_animalsnap.ui.MainActivity.Companion.userModel
import com.albertukrida.capstoneproject_animalsnap.ui.fragment.CameraFragment.Companion.alertDialog
import com.albertukrida.capstoneproject_animalsnap.ui.fragment.CameraFragment.Companion.mContext
import java.text.SimpleDateFormat
import java.util.*

class HistoryPagingSource(private val apiService: ApiService) : PagingSource<Int, DataItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getHistory(userModel.userId!!, page).data.toList()
            val sortedData = responseData.sortedBy { it.tanggalKlasifikasi.toDate() }

            alertDialog.dismiss()
            LoadResult.Page(
                data = sortedData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            alertDialog.dismiss()
            Toast.makeText(mContext, exception.toString(), Toast.LENGTH_LONG).show()
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun String.toDate(): Date{
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(this) as Date
    }
}