package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName

data class ProfileRequest (

    val matricule: String,

    @SerializedName("telephone")
    val telephone:String,

    @SerializedName("cin")
    val cin:String









)