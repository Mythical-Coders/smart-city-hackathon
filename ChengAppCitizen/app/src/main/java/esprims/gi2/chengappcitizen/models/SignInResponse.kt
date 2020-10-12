package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName

data class SignInResponse (

    @SerializedName("id")
    val id: String,

    @SerializedName("username")
    val userName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("roles")
    val roles: List<String>,

    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("tokenType")
    val tokenType: String

)