package esprims.gi2.chengappcitizen.classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*

class DbBitmapUtility(val context: Context) {

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
    //convert View to bit map
    fun getBitMapFromView(view: View):Bitmap{
        //Define a bitmap with the same size as the view
        val returnedBitmap:Bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)

        //Bind a canvas to it
        val canvas:Canvas = Canvas(returnedBitmap)

        //Get the view's background
        val drawable: Drawable = view.background
        drawable.draw(canvas)

        // draw the view on the canvas
        view.draw(canvas)

        //return
        return returnedBitmap
    }


    // convert bitmap to file
     fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(context.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    //create MultiPartBody.Part
     fun buildImageBodyPart(fileName: String, bitmap: Bitmap):  MultipartBody.Part
    {
        val leftImageFile = convertBitmapToFile(fileName, bitmap)
        val reqFile = leftImageFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fileName,     leftImageFile.name, reqFile)
    }

    //create MultiPartBody.Part from bit map
     fun callUploadImageService(image1: Bitmap){
        val image1Part = buildImageBodyPart("imageFile", image1)

        }

}
