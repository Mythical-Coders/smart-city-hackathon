package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName

data class SignUpRequest (



    @SerializedName("username")
    val userName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("roles")
    val roles: List<String>



)
