package com.example.mysubmission2.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mysubmission2.data.response.ListStoryItem
import com.example.mysubmission2.data.remote.Api
import com.example.mysubmission2.paging.Preference
import kotlinx.coroutines.flow.first

class StoryPagingSource (private val userPreference: Preference, private val apiService: Api): PagingSource<Int, ListStoryItem>() {

    // Code from Dicoding Module
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            //Adding Token Reference
            val token: String = userPreference.getUser().first().token
            val responseData = apiService.getAllStory("Bearer $token", page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}