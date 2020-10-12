package esprims.gi2.chengappcitizen.classes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class PhotoManager(context:Context) {

    private var photoDbHelper:PhotoDbHelper = PhotoDbHelper(context)

    lateinit var db: SQLiteDatabase

    fun openReadDB(){
        db = photoDbHelper.readableDatabase
    }
    fun openWriteDB(){
        db= photoDbHelper.writableDatabase
    }
    fun closeDB(){
        db.close()
    }
    fun addPhoto(photo: Photo):Long{
        val contentValues = ContentValues()
        contentValues.put(PhotoDbHelper.COL_TITLE,photo.title)
        contentValues.put(PhotoDbHelper.COL_IMAGE,photo.image)
        return photoDbHelper.writableDatabase.insert(PhotoDbHelper.THE_TABLE,null, contentValues)
    }
    fun getPhoto():List<Photo>{

        //declare variables
        val listPhoto:ArrayList<Photo> = ArrayList()
        val selectQuery = "SELECT * FROM "+PhotoDbHelper.THE_TABLE
        val cursor: Cursor?

        cursor = db.rawQuery(selectQuery,null)

        var title:String
        var image:ByteArray
        if(cursor.moveToFirst()){
            do{
                title = cursor.getString(cursor.getColumnIndex("title"))
                image = cursor.getBlob(cursor.getColumnIndex("image"))

                val p = Photo(title,image)
                listPhoto.add(p)

            }while(cursor.moveToNext())
        }
        return  listPhoto
    }





}