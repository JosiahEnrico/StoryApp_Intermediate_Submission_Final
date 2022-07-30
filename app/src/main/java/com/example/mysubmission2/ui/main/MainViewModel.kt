package com.example.mysubmission2.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mysubmission2.data.response.ListStoryItem
import com.example.mysubmission2.paging.data.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVIewModel @Inject constructor(repository: StoryRepository) : ViewModel(){
    private val checkLoading = MutableLiveData<Boolean>()
    val story: LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)
    val loading: LiveData<Boolean> = checkLoading
}