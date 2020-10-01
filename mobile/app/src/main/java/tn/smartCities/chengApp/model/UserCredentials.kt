package tn.smartCities.chengApp.model

import com.google.gson.annotations.SerializedName

data class UserCredentials (
    @SerializedName("username")
    val userName: String,
    @SerializedName("password")
    val password: String
)