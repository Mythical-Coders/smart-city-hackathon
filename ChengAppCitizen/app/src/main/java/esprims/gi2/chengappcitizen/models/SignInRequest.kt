package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName

data class SignInRequest (

    @SerializedName("username")
    val userName: String,
    @SerializedName("password")
    val password: String

)
