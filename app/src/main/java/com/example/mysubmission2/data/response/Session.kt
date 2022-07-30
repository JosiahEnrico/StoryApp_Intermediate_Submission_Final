package com.example.mysubmission2.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Session(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("isLogin")
    val isLogin: Boolean

) : Parcelable