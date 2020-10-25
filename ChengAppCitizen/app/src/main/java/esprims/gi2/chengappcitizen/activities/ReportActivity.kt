package esprims.gi2.chengappcitizen.activities

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import esprims.gi2.chengappcitizen.R
import esprims.gi2.chengappcitizen.adapters.Mapadapter
import esprims.gi2.chengappcitizen.adapters.PhotoAdapter
import esprims.gi2.chengappcitizen.adapters.ReportAdapter
import esprims.gi2.chengappcitizen.classes.DbBitmapUtility
import esprims.gi2.chengappcitizen.classes.Photo
import esprims.gi2.chengappcitizen.classes.PhotoManager
import esprims.gi2.chengappcitizen.models.*
import esprims.gi2.chengappcitizen.preference.AppPreference
import esprims.gi2.chengappcitizen.rest.ApiClient
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import java.util.*


class ReportActivity : AppCompatActivity(), CoroutineScope by MainScope() {


    //load the dbBitMapUtility for converting images type functions
    private val convert: DbBitmapUtility = DbBitmapUtility(this)

    // Local database manager for images
    private val photoManager: PhotoManager = PhotoManager(this)

    //init locationManager
    private var locationManager: LocationManager? = null

    //Store the latitude and longitude from the location listener
    var lat: String=""
    var long: String=""

    //variable to store the id of @post location
    var idLocation =""

    //variable to store the location address
    var adr=""

    //variable to store the id of @post photo
    var idPhoto =""

    //variable to the type of the report
    var type = ""

    var pressAgain=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


        //hide or show view depending radio checks
        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->

                if (R.id.radioOther == checkedId) {

                    text_desc.visibility = View.VISIBLE
                    descriptionId.visibility = View.VISIBLE
                } else {
                    text_desc.visibility = View.INVISIBLE
                    descriptionId.visibility = View.INVISIBLE
                }
            }
        )

        //This is the default status for take photo button, it will be enabled when you allow camera
        photo_btn.isEnabled = false

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                111
            )
        } else
            photo_btn.isEnabled = true


        //take photo
        photo_btn.setOnClickListener {

            //update the location and ask permission form user
            updateLocation()

            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)

        }


        //send Report
        sendReportId.setOnClickListener {


            //update the location again when permission is granted
            updateLocation()

            //call the location service and get idLocation
            addPlace()

            //declare variables for @POST method
            when {
                radioParking.isChecked -> {
                    type = "illegal Parking"
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
            if (type != "noValue" && pressAgain ==false) {


                //get the user's id
                val idUser: String = AppPreference.id


                launch(Dispatchers.Main) {



                    try {

                        //call the upload-photo service and get idPhoto
                        addPhoto()



                        //execute the report service
                        val reportRequest = ReportRequest(idUser, type, idPhoto, idLocation)
                        addReport(reportRequest) {
                            if (it != null) {
                                Toast.makeText(
                                    applicationContext,
                                    "تم إرسال البلاغ بنجاح",
                                    Toast.LENGTH_LONG
                                ).show()
                                setResult(Activity.RESULT_CANCELED)
                                finish()

                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "errorr !!!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    } catch (e: Exception) {

                        Toast.makeText(
                            applicationContext,
                            "Error Occurred report: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }

            }
            else if (type !="noValue" && pressAgain == true){
                Toast.makeText(
                    applicationContext,
                    "location registered please click again",
                    Toast.LENGTH_LONG
                ).show()
            }



        }

    }

    //When the camera intent is done, we change the visibility of either the done or error icon
    //and set the image view with the picture taken
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == -1) {
                done_icon.visibility = View.VISIBLE
                photoView.visibility = View.VISIBLE
                if (data != null) {

                    //set the image view with camera picture
                    val imageBitMap = data.extras?.get("data") as Bitmap
                    photoView.setImageBitmap(imageBitMap)


                    //convert image as a byteArray
                    val imageByte: ByteArray = convert.getbytes(imageBitMap)
                    //add title to db as the type of the report
                    when {
                        radioParking.isChecked -> {
                            type = "نوعية المخالفة: توقف مخالف للقانون"
                        }

                        radioOther.isChecked -> {

                            type = descriptionId.text.toString() + "نوعية المخالفة:"
                        }
                    }
                    val photo = Photo(type,imageByte) //add typeReportLater
                    photoManager.openWriteDB()
                    photoManager.addPhoto(photo)
                    photoManager.closeDB()
                } else {
                    Toast.makeText(applicationContext, "did not read intent", Toast.LENGTH_LONG).show()
                }

            } else
                error_icon.visibility = View.VISIBLE
        }
       /* if (requestCode == 222) {

            postionText.visibility = View.VISIBLE
            Toast.makeText(applicationContext, "location granted", Toast.LENGTH_LONG).show()
            postionText.text = "latitude: |" + lat + " longtitude: " + long

        }
        else{
            postionText.text  =  "location denied"
        }*/

    }

    //When Camera permission is granted, the button will be enabled
    //when position permission is granted, a text view will show the location status
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        /*super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            photo_btn.isEnabled = true
        */
        fun innerCheck(name:String){
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext," $name permission refused",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext," $name permission granted",Toast.LENGTH_SHORT).show()
                if(name == "camera"){
                    photo_btn.isEnabled = true
                }
                if(name =="Location"){
                    updateLocation()
                }
            }
        }
        when(requestCode){
            111-> innerCheck("camera")
            222-> innerCheck("Location")
        }

    }



    private fun addReport(reportRequest: ReportRequest, onResult: (ReportResponse?) -> Unit) {
        val retrofit = ReportAdapter.buildService(ApiClient::class.java)
        retrofit.addReport(reportRequest).enqueue(
            object : retrofit2.Callback<ReportResponse> {
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


    private fun addPlaceService(mapRequest: MapRequest, onResult: (MapResponse?) -> Unit) {
        val retrofit = Mapadapter.buildService(ApiClient::class.java)
        retrofit.addPlace(mapRequest).enqueue(
            object : retrofit2.Callback<MapResponse> {
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
    private fun updateLocation(){

        //Permission check for Location
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {

          /*  ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 222)*/
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),222)
        }
        else {

            //This is for extracting the location
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

            //Link the location manager with location listener to get location as soon as it changed
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
            //get the address
            if(lat !="" && long != "") {
                val geocoder = Geocoder(applicationContext, Locale.getDefault())

                adr = (geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)).toString()
            }
            else{
                adr="location listener didn't trigger"
            }


        }

    }

    //function to post the location and return id of the @POST method
    private fun addPlace() {

        // aux variable to store the location id
        var idMap = "initValue"

        //variables for @POST
        val typeLocation = "report Place"
        if (lat!="" && long!="") {

            pressAgain = false

            launch(Dispatchers.Main) {
                try {

                    val mapRequest = MapRequest(typeLocation, long.toDouble(), lat.toDouble(), adr)
                    addPlaceService(mapRequest) {
                        if (it != null) {
                            idLocation = it.id

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
        else{
            pressAgain = true
        }


    }

    private suspend fun addPhoto() {

        // aux variable to store the photo id
        var id=""

        // call convert functions
        val imageName: String = "image name test"
        val imageView: View = photoView.findViewById<View>(R.id.photoView)
        val imageBitMap: Bitmap = convert.getBitMapFromView(imageView)
        val imageFile = convert.buildImageBodyPart("imageFile", imageBitMap)

        coroutineScope {
            launch(Dispatchers.IO) {
                try {
                    val photoRequest = PhotoRequest(imageName, imageFile)
                    addPhotoService(photoRequest) {
                            if(it!="failed") {
                                id = it
                            }
                        else{
                                id = "image not found"
                            }
                    }
                    delay(1000)
                    idPhoto = withContext(Dispatchers.Default) {
                            id
                        }

                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Error Occurred photo: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }



    }

    private fun addPhotoService(photoRequest: PhotoRequest, onresult: (String) -> Unit) {
        val retrofit = PhotoAdapter.buildService(ApiClient::class.java)
        retrofit.addPhoto(photoRequest.imageName, photoRequest.imageFile).enqueue(
            object : retrofit2.Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    onresult("failed")
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                        val id = response.body()
                        onresult(id.toString())
                }

            }
        )
    }

}





