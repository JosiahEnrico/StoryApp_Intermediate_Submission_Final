package com.example.mysubmission2.ui.newStory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysubmission2.R
import com.example.mysubmission2.data.remote.Api
import com.example.mysubmission2.data.response.AddResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewStoryViewModel @Inject constructor(private val apiService: Api): ViewModel() {

    private val message = MutableLiveData<String>()
    val errorMessage: LiveData<String> = message

    fun uploadStory(token: String, photo: File, description: String, latitude: RequestBody? = null, longitude: RequestBody? = null) {
        val descriptionText = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )
        apiService.addNewStory(token, imageMultipart, descriptionText, latitude, longitude)
            .enqueue(object : Callback<AddResponse> {
                override fun onResponse(call: Call<AddResponse>, response: Response<AddResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            message.value = responseBody.message
                            Log.d(RETROFIT, response.body()?.message.toString())
                        } else {
                            message.value =  response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<AddResponse>, t: Throwable) {
                    message.value = ("" + R.string.retrofit_failure)
                    Log.d(RETROFIT, t.message.toString())
                }

            })
    }

    companion object{
        const val RETROFIT = "Retrofit"
    }

}