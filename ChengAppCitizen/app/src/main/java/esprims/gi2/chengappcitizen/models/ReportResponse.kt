package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class ReportResponse (

    @SerializedName("id")
    val id:String,

    @SerializedName("idUser")
    val idUser:String,

    @SerializedName("type")
    val type:String,

    @SerializedName("idImage")
    val idImage:String,

    @SerializedName("idPlace")
    val idPlace:String,

    @SerializedName("createDate")
    val createDate:Date



)