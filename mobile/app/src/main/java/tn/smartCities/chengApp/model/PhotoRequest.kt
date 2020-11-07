package tn.smartCities.chengApp.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class PhotoRequest (
    @SerializedName("image")
    val image: MultipartBody.Part
)