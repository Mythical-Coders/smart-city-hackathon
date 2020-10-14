package esprims.gi2.chengappcitizen.activities

import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import esprims.gi2.chengappcitizen.R
import esprims.gi2.chengappcitizen.classes.Photo
import esprims.gi2.chengappcitizen.classes.PhotoManager

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val photoManager: PhotoManager = PhotoManager(this)
    private var list : List<Photo> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //fill the list from database
        list = this.refresh()

        report_btn.setOnClickListener{

            intent = Intent(applicationContext,ReportActivity::class.java)
            startActivity(intent)


        }

    }
    private fun refresh(): List<Photo> {

        photoManager.openReadDB()
        list=photoManager.getPhoto()
        list_id.adapter = ArrayAdapter<Photo>(this,android.R.layout.simple_list_item_1,list)

        photoManager.closeDB()
        return list

    }

}