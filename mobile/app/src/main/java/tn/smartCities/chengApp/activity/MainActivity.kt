package tn.smartCities.chengApp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.telephony.SmsManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.smartCities.chengApp.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_timer.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.smartCities.chengApp.adapter.CitizenApiAdapter
import tn.smartCities.chengApp.adapter.ImpoundApiAdapter
import tn.smartCities.chengApp.adapter.PhotoAdapter
import tn.smartCities.chengApp.model.*
import tn.smartCities.chengApp.preference.AppPreferences
import tn.smartCities.chengApp.rest.ApiClient
import tn.smartCities.chengApp.util.DbBitmapUtility
import tn.smartCities.chengApp.util.PrefUtil
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import kotlin.time.minutes

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    //load the dbBitMapUtility for converting images type functions
    private val convert: DbBitmapUtility = DbBitmapUtility(this)

    private var selectedImagesPaths // Paths of the image(s) selected by the user.
            : ArrayList<String>? = null
    private var imagesSelected = false

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 300L
    private var secondsRemaining: Long = 300L
    private var locationManager: LocationManager? = null

    //Location string used to store the lattitude and logitude from the lcoation listener
    var location: String = ""

    //Message to be sent to someone to notify him along with location
    var msg = "لديك خمسة دقائق لتغير مكان سيارتك أو سيقع شنقلتها، "

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري تسجيل المخالفة ...")
        progressDialog.setCancelable(true)

        //This is for extracting the location
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        //Add the customized action bar
        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        val view = supportActionBar?.customView
        //Timer start time
        timer_id.text = getString(R.string.timer_start)
        //Citizen object to be assigned when calling the API
        var citizen: Citizen = Citizen()
        var listPlaces: List<String> = listOf("")

        val placesContext = newSingleThreadContext("PlacesContext")
        var places: List<PlaceResponse> = listOf()

        //val date:Date = Date.from(Instant.now())
        val date = LocalDateTime.now()

        //Permission check for Location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                222
            )
        }
        //Link the location manager with location listener to get location as soon as it changed
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener
        )
        //Default status for the done and error icon next to take photo button
        done_icon_front.visibility = View.INVISIBLE
        error_icon_front.visibility = View.INVISIBLE
        done_icon_back.visibility = View.INVISIBLE
        error_icon_back.visibility = View.INVISIBLE

        carImage.visibility = View.INVISIBLE


        //When clicking on notify it appends the location to the msg and call the API to extract the citizen info and send sms
        notify_btn.setOnClickListener {
            msg += location
            launch(Dispatchers.Main) {
                try {
                    val response =
                        CitizenApiAdapter.apiClient.getCitizenByMatricule(licensePlate.text.toString())
                    citizen = response.body()!!
                    sendSms(response.body()?.telephone.toString(), msg)
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        //When click sign out it returns to the welcome activity
        sign_out.setOnClickListener {
            intent = Intent(applicationContext, WelcomeActivity::class.java)
            AppPreferences.isLogin = false
            AppPreferences.password = ""
            AppPreferences.username = ""
            startActivity(intent)
        }

        //This is the default status for take photo button, it will be enabled when you allow camera
        take_front_photo.isEnabled = false
        take_back_photo.isEnabled = false
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
            take_front_photo.isEnabled = true
        take_back_photo.isEnabled = true

        take_front_photo.setOnClickListener {
            /*
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)*/
            val intent = Intent()
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                103
            )
        }

        take_back_photo.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 102)
        }

        send_btn.setOnClickListener {
            val impound = Impound(
                0,
                "",
                AppPreferences.id,
                "Place 1",
                date.toString() + "Z",
                licensePlate.text.toString(),
                false,
                "",
                "",
                false,
                citizen.telephone
            )
            launch(Dispatchers.Main) {
                try {
                    progressDialog.show()
                    addImpound(impound) {
                        if (it == 200) {
                            Toast.makeText(
                                applicationContext,
                                "تم تسجيل المخالفة",
                                Toast.LENGTH_LONG
                            ).show()
                            progressDialog.dismiss()
                        } else {

                            Toast.makeText(
                                applicationContext,
                                "الرجاء التثبت من المعطيات",
                                Toast.LENGTH_LONG
                            ).show()
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

    //When the camera intent is done, we change the visibility of either the done or error icon
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                done_icon_front.visibility = View.VISIBLE
                val imageBitMap = data.extras?.get("data") as Bitmap
                carImage.setImageBitmap(imageBitMap)

                val imageFile = convert.buildImageBodyPart("image", imageBitMap)
                    try {
                        val photoRequest = PhotoRequest( imageFile)
                        addPhotoService(imageFile) {
                            licensePlate.setText(it)
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            applicationContext,
                            "Error Occurred photo: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } else {
                error_icon_front.visibility = View.VISIBLE
            }
        }
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                done_icon_back.visibility = View.VISIBLE
            } else {
                error_icon_back.visibility = View.VISIBLE
            }
        }
        if (requestCode === 103 && resultCode === Activity.RESULT_OK && null != data) {
            // When a single image is selected.
            done_icon_front.visibility = View.VISIBLE
            var currentImagePath: String
            selectedImagesPaths = ArrayList()
            val uri = data.data
            currentImagePath = uri?.let { getPath(applicationContext, it) }.toString()
            Log.d("ImageDetails", "Single Image URI : $uri")
            Log.d("ImageDetails", "Single Image Path : $currentImagePath")
            selectedImagesPaths!!.add(currentImagePath)
            imagesSelected = true
            connectServer()
        } else {
            Toast.makeText(this, "You haven't Picked any Image.", Toast.LENGTH_LONG).show()
        }
        Toast.makeText(
            applicationContext,
            selectedImagesPaths?.size.toString() + " Image(s) Selected.",
            Toast.LENGTH_LONG
        ).show()
    }

    //When Camera permission is granted the button will be enabled
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            take_front_photo.isEnabled = true
        take_back_photo.isEnabled = true

    }

    //Start timer function
    private fun startTimer() {
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() {
                onTimerFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
        notify_btn.isEnabled = false
        send_btn.isEnabled = false
    }

    //When the timer finish
    private fun onTimerFinished() {
        progress_countdown.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateCountdownUI()

        notify_btn.isEnabled = true
        send_btn.isEnabled = true
    }

    //Update the count down UI
    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        timer_id.text =
            "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        progress_countdown.progress = ((timerLengthSeconds - secondsRemaining) / 3).toInt()
    }

    //Define the location listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            this@MainActivity.location = "" + location.longitude + "," + location.latitude
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    //Define the pending Intent for sending and delivering the sms along with the SmsManager
    private fun sendSms(number: String, message: String) {
        val smsManager: SmsManager = SmsManager.getDefault()
        val parts: ArrayList<String> = smsManager.divideMessage(message)
        val numParts = parts.size

        val SENT: String = "SMS_SENT"
        val DELIVERED: String = "SMS_DELIVERED"

        val sentPI = ArrayList<PendingIntent>()
        val deliveredPI = ArrayList<PendingIntent>()

        for (i in 0 until numParts) {
            sentPI.add(PendingIntent.getBroadcast(this, 0, Intent(SENT), 0))
            deliveredPI.add(PendingIntent.getBroadcast(this, 0, Intent(DELIVERED), 0))
        }

        val sendSMS: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        startTimer()
                        Toasty.success(
                            this@MainActivity,
                            "SMS sent success!",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE ->
                        Toasty.error(
                            this@MainActivity,
                            "No active network to send SMS.",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                    SmsManager.RESULT_ERROR_RADIO_OFF ->
                        Toasty.error(
                            this@MainActivity,
                            "SMS not sent 1!",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE ->
                        Toasty.error(
                            this@MainActivity,
                            "SMS not sent 2!",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                    SmsManager.RESULT_ERROR_NULL_PDU ->
                        Toasty.error(
                            this@MainActivity,
                            "SMS not sent! 3",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                }
            }
        }

        val deliverSMS: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        Toasty.success(
                            this@MainActivity,
                            "SMS delivered.",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                    Activity.RESULT_CANCELED ->
                        Toasty.error(
                            this@MainActivity,
                            "SMS not delivered.",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                }
            }

        }

        registerReceiver(sendSMS, IntentFilter(SENT))
        registerReceiver(deliverSMS, IntentFilter(DELIVERED))

        smsManager.sendMultipartTextMessage(number, null, parts, sentPI, deliveredPI)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun addImpound(impound: Impound, onResult: (Int?) -> Unit) {
        val retrofit = ImpoundApiAdapter.buildService(ApiClient::class.java)
        retrofit.addImpound(impound).enqueue(
            object : Callback<Impound> {
                override fun onFailure(call: Call<Impound>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<Impound>, response: Response<Impound>) {
                    onResult(response.code())
                }
            }
        )
    }
    /*
    private fun getPlaces(onResult: (List<PlaceResponse>) -> Unit){
        val retrofit = PlaceApiAdapter.buildService(ApiClient::class.java)
        retrofit.getPlaces().enqueue(
            object: Callback<List<PlaceResponse>>{
                override fun onFailure(call: Call<List<PlaceResponse>>, t: Throwable) {
                    onResult(emptyList())
                }

                override fun onResponse(
                    call: Call<List<PlaceResponse>>,
                    response: Response<List<PlaceResponse>>
                ) {
                    response.body()?.let { onResult(it) }
                }

            }
        )
    }*/

    private fun addPhotoService(photoRequest: MultipartBody.Part, onResult: (String) -> Unit) {
        val retrofit = PhotoAdapter.buildService(ApiClient::class.java)
        retrofit.addPhoto(photoRequest).enqueue(
            object : Callback<PhotoResponse> {
                override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                    onResult("failed")
                }

                override fun onResponse(
                    call: Call<PhotoResponse>,
                    response: Response<PhotoResponse>
                ) {
                    onResult(response.message())
                }

            }
        )
    }

    private fun getPath(context: Context?, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory()
                        .toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context!!, contentUri, "", arrayOf())
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context!!, contentUri!!, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context!!, uri, "", arrayOf())
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri, selection: String,
        selectionArgs: Array<String>
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun postRequest(postUrl: String, postBody: RequestBody) {
        val client = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .build()
        val request: Request = Request.Builder()
            .method("POST", postBody)
            .url(postUrl)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    call.cancel()
                    Log.d("FAIL", e.message)

                    // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                    runOnUiThread {
                        val responseText: TextView = findViewById(R.id.licensePlate)
                        responseText.text = e.toString()
                    }
                }
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    runOnUiThread {
                        val responseText: TextView = findViewById(R.id.licensePlate)
                        try {
                            responseText.text = response.body?.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        })
    }
    private fun connectServer() {
        val responseText: TextView = findViewById(R.id.licensePlate)

        responseText.text = "Sending the Files. Please Wait ..."

        val postUrl = "https://f5066d910caf.ngrok.io/predict"
        val multipartBodyBuilder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (i in selectedImagesPaths!!.indices) {
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.RGB_565
            val stream = ByteArrayOutputStream()
            try {
                // Read BitMap by file path.
                val bitmap: Bitmap = BitmapFactory.decodeFile(selectedImagesPaths!![i], options)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            } catch (e: java.lang.Exception) {
                responseText.text = "Please Make Sure the Selected File is an Image."
                return
            }
            val byteArray: ByteArray = stream.toByteArray()
            multipartBodyBuilder.addFormDataPart(
                "image",
                "Android_Flask_$i.jpg",
                RequestBody.create("image/*jpg".toMediaTypeOrNull(), byteArray)
            )
        }
        val postBodyImage: RequestBody = multipartBodyBuilder.build()
        postRequest(postUrl, postBodyImage)
    }
}


