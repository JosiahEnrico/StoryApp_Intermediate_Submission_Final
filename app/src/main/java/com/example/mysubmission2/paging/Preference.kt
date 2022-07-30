package com.example.mysubmission2.paging

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mysubmission2.data.response.Session
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class Preference @Inject constructor(@ApplicationContext val context: Context) {

    private val dataStore = context.dataStore

    fun getUser(): Flow<Session> {
        return dataStore.data.map { preferences ->
            Session(
                preferences[NAME_KEY] ?:"",
                preferences[TOKEN] ?:"",
                preferences[USER_ID_KEY] ?:"",
                preferences[LOGIN_STATE] ?: false
            )
        }
    }

    suspend fun setUser(user: Session) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[TOKEN] = user.token
            preferences[USER_ID_KEY] = user.userId
            preferences[LOGIN_STATE] = user.isLogin
        }
    }


    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = ""
            preferences[TOKEN] = ""
            preferences[USER_ID_KEY] = ""
            preferences[LOGIN_STATE] = false
        }
    }

    companion object {

        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")
        private val USER_ID_KEY = stringPreferencesKey("password")
        private val LOGIN_STATE = booleanPreferencesKey("state")

    }
}