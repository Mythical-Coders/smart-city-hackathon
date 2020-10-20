package esprims.gi2.chengappcitizen.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class PhotoResponse (

    @SerializedName("id")
    val id:String,

    @SerializedName("title")
    val title:String,


    @SerializedName("image")
    val image:ByteArray,

    @SerializedName("createDate")
    val createDate: Date




) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoResponse

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}