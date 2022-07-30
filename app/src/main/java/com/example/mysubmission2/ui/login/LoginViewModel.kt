package com.example.mysubmission2.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysubmission2.data.remote.Api
import com.example.mysubmission2.data.response.LoginResponse
import com.example.mysubmission2.data.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val apiService: Api) : ViewModel() {

    private val checkLogin = MutableLiveData<User>()
    private val checkLoading = MutableLiveData<Boolean>()
    private val message = MutableLiveData<String>()
    val login: LiveData<User> = checkLogin
    val loading: LiveData<Boolean> = checkLoading
    val errorMessage: LiveData<String> = message

    fun loginUser(email: String, password: String) {
        checkLoading.value = true
        apiService.userLogin(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    checkLoading.value = false
                    if (response.isSuccessful) {
                        message.value = response.body()?.message
                        checkLogin.value = response.body()?.loginResult
                        Log.d(RETROFIT, response.body()?.message.toString())
                        Log.d(RETROFIT, response.body()?.loginResult?.token.toString())
                        Log.d(RETROFIT, response.body()?.loginResult?.name ?: "name")
                        Log.d(RETROFIT, response.body()?.loginResult?.userId ?: "userId")
                    }
                    if (!response.isSuccessful) {
                        message.value = response.message()
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    message.value = t.message
                    checkLoading.value = false
                    Log.d(RETROFIT, t.message.toString())
                }

            })
    }

    companion object{
        const val RETROFIT = "Retrofit"
    }

}