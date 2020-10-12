package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName

data class MapResponse (

    @SerializedName("id")
    val id:String,

    @SerializedName("type")
    val type:String,

    @SerializedName("longitude")
    val longitude:Double,

    @SerializedName("latitude")
    val latitude:Double,

    @SerializedName("adress")
    val adress:String

    )