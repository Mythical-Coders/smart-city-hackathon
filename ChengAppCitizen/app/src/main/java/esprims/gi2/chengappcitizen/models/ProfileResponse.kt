package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse (

    @SerializedName("id")
    val id:String,

    @SerializedName("cin")
    val cin:String,

    @SerializedName("matricule")
    val matricule:String,

    @SerializedName("telephone")
    val telephone:String



)


