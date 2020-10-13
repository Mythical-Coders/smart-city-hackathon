package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName

data class ReportResponse (

    @SerializedName("id")
    val id:String,

    @SerializedName("type")
    val type:String,

    @SerializedName("idPlace")
    val idPlace:String
)