package tn.smartCities.chengApp.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.google.gson.annotations.SerializedName
import java.time.format.DateTimeFormatter
import java.util.*

data class Impound(

    @SerializedName("cin")
    val cin: Int,

    @SerializedName("id")
    val id: String,

    @SerializedName("idDriver")
    val idDriver: String,

    @SerializedName("idPlace")
    val idPlace: String,

    @SerializedName("impoundDate")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    val impoundDate: Date,

    @SerializedName("matricule")
    val matricule: String,

    @SerializedName("paid")
    val paid: Boolean,

    @SerializedName("paidDate")
    val paidDate: Date,

    @SerializedName("releaseDate")
    val releaseDate: Date,

    @SerializedName("released")
    val released: Boolean,

    @SerializedName("telephone")
    val telephone: Int
)