package com.example.mysubmission2.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mysubmission2.data.response.Session
import com.example.mysubmission2.paging.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val preference: Preference): ViewModel() {

    fun getSession(): LiveData<Session> {
        return preference.getUser().asLiveData()
    }

    fun setSession(user: Session) {
        viewModelScope.launch {
            preference.setUser(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
}