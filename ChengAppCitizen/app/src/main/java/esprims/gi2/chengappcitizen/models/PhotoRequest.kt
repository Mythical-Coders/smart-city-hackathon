package esprims.gi2.chengappcitizen.models

import com.fasterxml.jackson.databind.node.BinaryNode
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import java.util.function.BinaryOperator

data class PhotoRequest (
    @SerializedName("imageName")
    val imageName:String,

    @SerializedName("imageFile")
    val imageFile:MultipartBody


)