package tn.smartCities.chengApp.model

import com.google.gson.annotations.SerializedName

data class Citizen(

    @SerializedName("id")
    val id: String,

    @SerializedName("matricule")
    val matricule: String,

    @SerializedName("telephone")
    val telephone: Int
){
    constructor(): this("","",0)
}