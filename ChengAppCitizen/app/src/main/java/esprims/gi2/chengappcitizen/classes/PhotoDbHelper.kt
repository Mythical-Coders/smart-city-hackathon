package esprims.gi2.chengappcitizen.classes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PhotoDbHelper(context : Context?) : SQLiteOpenHelper(context , "Upload_Photo.db", null,1) {

    companion object{
        val THE_TABLE = "Tphoto"
        val COL_TITLE ="title"
        val COL_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PHOTO_TABLE=("CREATE TABLE "
                + THE_TABLE+" ("
                + COL_TITLE+" TEXT PRIMARY KEY,"
                + COL_IMAGE+" BLOB NOT NULL"+")")
        db?.execSQL(CREATE_PHOTO_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS "+ THE_TABLE)
        onCreate(db)
    }
}