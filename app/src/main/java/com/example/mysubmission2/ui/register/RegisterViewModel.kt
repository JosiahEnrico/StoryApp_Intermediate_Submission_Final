package com.example.mysubmission2.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysubmission2.data.remote.Api
import com.example.mysubmission2.data.response.RegisterResponse
import com.example.mysubmission2.data.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val apiService: Api) : ViewModel() {

    private val checkLogin = MutableLiveData<User>()
    private val checkLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = checkLoading
    val login: LiveData<User> = checkLogin

    fun registerUser(name: String, email: String,password: String) {
        val message = MutableLiveData<String>()
        checkLoading.value = true
        apiService.userRegister(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    checkLoading.value = false
                    if (response.isSuccessful) {
                        message.value = response.body()?.message
                        Log.d(RETROFIT, response.body()?.message.toString())
                    } else{
                        message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    checkLoading.value = false
                    message.value = t.message
                    Log.d(RETROFIT, t.message.toString())
                }
            })
    }

    companion object{
        const val RETROFIT = "Retrofit"
    }

}