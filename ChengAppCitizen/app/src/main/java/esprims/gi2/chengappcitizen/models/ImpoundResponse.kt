package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class ImpoundResponse (

    @SerializedName("id")
    val id:String,

    @SerializedName("title")
    val title:String,

    @SerializedName("description")
    val description:Int,

    @SerializedName("idReceiver")
    val idReceiver:String,

    @SerializedName("idSender")
    val idSender:String,

    @SerializedName("body")
    val body:String,

    @SerializedName("object")
    val objectVar:String,

    @SerializedName("type")
    val type:String,

    @SerializedName("date")
    val date:Date,

    @SerializedName("seen")
    val seen:Boolean


)