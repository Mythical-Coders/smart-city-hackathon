package esprims.gi2.chengappcitizen.activities

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import esprims.gi2.chengappcitizen.R
import esprims.gi2.chengappcitizen.adapters.PhotoAdapter
import esprims.gi2.chengappcitizen.adapters.ReportAdapter
import esprims.gi2.chengappcitizen.classes.DbBitmapUtility
import esprims.gi2.chengappcitizen.classes.Photo
import esprims.gi2.chengappcitizen.classes.PhotoManager
import esprims.gi2.chengappcitizen.rest.ApiClient
import kotlinx.android.synthetic.main.activity_report.*
import retrofit2.Call
import retrofit2.Response
import android.Manifest
import android.app.Activity
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import esprims.gi2.chengappcitizen.adapters.Mapadapter
import esprims.gi2.chengappcitizen.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*


class ReportActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    //variables for database
    val convert:DbBitmapUtility = DbBitmapUtility()
    val photoManager:PhotoManager = PhotoManager(this)

    //init locationManager
    private var locationManager : LocationManager? = null

    //Location string used to store the lattitude and logitude from the lcoation listener
    var lat: String = ""
    var long: String = ""

    //variable to store the id of @post locatio
    var idLocation:String =""

    //variable to the type of the report
    var type=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


        //hide or show view deppending radio checks
        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener{ group,checkedId ->

                if (R.id.radioOther==checkedId){

                    text_desc.visibility = View.VISIBLE
                    descriptionId.visibility = View.VISIBLE
                }
                else{
                    text_desc.visibility = View.INVISIBLE
                    descriptionId.visibility = View.INVISIBLE
                    }
                }
            )

        //This is the default status for take photo button, it will be enabled when you allow camera
        photo_btn.isEnabled = false
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 111)
        }
        else
            photo_btn.isEnabled = true


        //take photo
        photo_btn.setOnClickListener {


            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)

        }





        //send Report
        sendReportId.setOnClickListener {

            //declare variables for @POST method

            when {
                radioParking.isChecked -> {
                    type = "illegal Parking"
                }
                radioaccident.isChecked -> {
                    type = "accident"
                }
                radioOther.isChecked -> {

                    type = descriptionId.text.toString()
                }


                else -> {
                    Toast.makeText(
                        applicationContext,
                        "الرجاء حدد نوعية المخالفة",
                        Toast.LENGTH_LONG
                    ).show()
                    type = "noValue"
                }
            }
            if (type != "noValue") {

                val idPLace: String = addPlace()
                val idImage: String = addPhoto()

                launch(Dispatchers.Main) {
                    try {
                        val reportRequest = ReportRequest(type, idPLace, idImage)
                        addReport(reportRequest) {
                            if (it != null) {
                                Toast.makeText(
                                    applicationContext,
                                    "تم إرسال البلاغ بنجاح",
                                    Toast.LENGTH_LONG
                                ).show()
                                setResult(Activity.RESULT_CANCELED)
                                finish()

                            }
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            applicationContext,
                            "Error Occurred: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        }

    }

    //When the camera intent is done, we change the visibility of either the done or error icon
    //and set the image view with the picture taken
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101){
            if(resultCode == -1){
                done_icon.visibility = View.VISIBLE
                photoView.visibility = View.VISIBLE
                if (data !=null){

                    //set the image view with camera picture
                    val imageBitMap = data.extras?.get("data") as Bitmap
                    photoView.setImageBitmap(imageBitMap)


                    //store image in database
                    val imageByte:ByteArray =convert.getbytes(imageBitMap)
                    val photo = Photo("ghi",imageByte) //add typeReportLater
                    photoManager.openWriteDB()
                    photoManager.addPhoto(photo)
                    photoManager.closeDB()
                }
                else{
                    Toast.makeText(applicationContext,"did not read intent",Toast.LENGTH_LONG).show()
                }

            }

            else
                error_icon.visibility = View.VISIBLE
        }
    }
    //When Camera permission is granted the button will be enabled
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            photo_btn.isEnabled = true
    }

    private fun addPhotoService(photoRequest: PhotoRequest): String{
        //To be added after the next commit
        val retrofit = PhotoAdapter.buildService((ApiClient::class.java))
        return  "id"

    }

    private fun addReport(reportRequest: ReportRequest,onResult:(ReportResponse?)->Unit){
        val retrofit = ReportAdapter.buildService(ApiClient::class.java)
        retrofit.addReport(reportRequest).enqueue(
            object : retrofit2.Callback<ReportResponse>{
                override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<ReportResponse>,
                    response: Response<ReportResponse>
                ) {
                   val addReport = response.body()
                    onResult(addReport)
                }

            }
        )
    }

    //Define the location listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            this@ReportActivity.lat = "" + location.latitude
            this@ReportActivity.long = "" + location.longitude
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    private fun addPlaceService(mapRequest: MapRequest,onResult:(MapResponse?)->Unit){
        val retrofit = Mapadapter.buildService(ApiClient::class.java)
        retrofit.addPlace(mapRequest).enqueue(
            object : retrofit2.Callback<MapResponse>{
                override fun onFailure(call: Call<MapResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<MapResponse>,
                    response: Response<MapResponse>
                ) {
                    val addPlace = response.body()
                    onResult(addPlace)
                }

            }
        )
    }

    //function to post the location and return id of the @POST method
    private fun addPlace():String{


        //This is for extracting the location
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        //Permission check for Location
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 222)
        }

        //Link the location manager with location listener to get location as soon as it changed
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        if(lat !="" && long!="") {
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            val address = geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)

            //variables for @POST
            val type = "report"
            val adr = address.toString()

            launch(Dispatchers.Main) {
                try {

                    val mapRequest = MapRequest(type,long.toDouble(),lat.toDouble(),adr)
                    addPlaceService(mapRequest){
                        if(it!=null){
                            idLocation = it.id

                        }

                    }
                }catch(e: Exception){
                    Toast.makeText(applicationContext,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }

        }
        else{
            return ""
        }
        return idLocation

    }
    fun addPhoto():String{

        //implement addPhotoservice
        //travail next commit

        return "id" //return id from @POST
    }

}





