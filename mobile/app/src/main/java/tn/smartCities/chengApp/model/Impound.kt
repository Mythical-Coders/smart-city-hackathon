package tn.smartCities.chengApp.model

import com.google.gson.annotations.SerializedName

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
    val impoundDate: String,

    @SerializedName("matricule")
    val matricule: String,

    @SerializedName("paid")
    val paid: Boolean,

    @SerializedName("paidDate")
    val paidDate: String,

    @SerializedName("releaseDate")
    val releaseDate: String,

    @SerializedName("released")
    val released: Boolean,

    @SerializedName("telephone")
    val telephone: Int
)