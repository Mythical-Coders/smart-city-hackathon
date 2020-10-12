package esprims.gi2.chengappcitizen.classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class DbBitmapUtility {

    //convert from BitMap to BytesArray
    fun getbytes(bitMap: Bitmap):ByteArray {
        val stream: ByteArrayOutputStream = ByteArrayOutputStream()
        bitMap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }


    //convert from ByteArrays tp BitMap
    fun getBitMap(byteArray:ByteArray):Bitmap{

        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
}
