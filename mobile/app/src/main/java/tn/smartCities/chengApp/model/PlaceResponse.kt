package tn.smartCities.chengApp.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse (
  @SerializedName("address")
  val address: String,

  @SerializedName("createDate")
  val createDate: String,

  @SerializedName("id")
  val id: String,

  @SerializedName("latitude")
  val latitude: Double,

  @SerializedName("longitude")
  val longitude: Double,

  @SerializedName("name")
  val name: String,

  @SerializedName("postCode")
  val postCode: Int,

  @SerializedName("region")
  val region: String,

  @SerializedName("ville")
  val ville: String

){constructor(): this("","","", 0.0,0.0,"",0,"","")}
