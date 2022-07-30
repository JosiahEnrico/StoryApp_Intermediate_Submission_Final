package com.example.mysubmission2.paging.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.mysubmission2.data.local.StoryDatabase
import com.example.mysubmission2.data.response.*
import com.example.mysubmission2.data.remote.Api
import com.example.mysubmission2.paging.Preference
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: Api,
    private val preference: Preference
    ) {

    // Code from Dicoding Module
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase,apiService, preference),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}