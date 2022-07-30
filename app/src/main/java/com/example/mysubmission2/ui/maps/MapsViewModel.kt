package com.example.mysubmission2.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysubmission2.data.remote.Api
import com.example.mysubmission2.data.response.ListStoryItem
import com.example.mysubmission2.data.response.StoriesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val apiService: Api) : ViewModel() {

    private val checkStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = checkStory

    fun getStoryWithLocation(token: String) {
        apiService.getAllStoryMaps(token, 1)
            .enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        checkStory.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    Log.d(RETROFIT, t.message.toString())
                }

            })
    }

    companion object{
        const val RETROFIT = "Retrofit"
    }

}