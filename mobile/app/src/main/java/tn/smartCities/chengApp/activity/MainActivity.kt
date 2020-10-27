package tn.smartCities.chengApp.activity

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.telephony.SmsManager
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.smartCities.chengApp.adapter.CitizenApiAdapter
import tn.smartCities.chengApp.adapter.ImpoundApiAdapter
import tn.smartCities.chengApp.adapter.PlaceApiAdapter
import tn.smartCities.chengApp.model.Citizen
import tn.smartCities.chengApp.model.Impound
import tn.smartCities.chengApp.model.PlaceResponse
import tn.smartCities.chengApp.preference.AppPreferences
import tn.smartCities.chengApp.rest.ApiClient
import tn.smartCities.chengApp.util.PrefUtil
import java.time.LocalDateTime

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 300L
    private var secondsRemaining: Long = 300L
    private var locationManager: LocationManager? = null

    //Location string used to store the lattitude and logitude from the lcoation listener
    var location: String = ""

    //Message to be sent to someone to notify him along with location
    var msg = "لديك خمسة دقائق لتغير مكان سيارتك أو سيقع شنقلتها، "

    var bitmap: Bitmap? = null

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

        //When clicking on notify it appends the location to the msg and call the API to extract the citizen info and send sms
        notify_btn.setOnClickListener {
            msg += location
            launch(Dispatchers.Main) {
                try {
                    val response = CitizenApiAdapter.apiClient.getCitizenByMatricule(licensePlate.text.toString())
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
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)
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

        launch(Dispatchers.IO){
            try {
                val response = PlaceApiAdapter.apiClient.getPlaces()
                withContext(placesContext){
                    places = response.body()!!
                }

                /*
                getPlaces(){

                    listPlaces = listOf(it[0].name, it[1].name)
                    val placeSpinner = findViewById<Spinner>(R.id.placeSpinner)
                    var placeSelected:String = ""
                    if (placeSpinner != null) {
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listPlaces)
                        placeSpinner.adapter = adapter
                        placeSpinner.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                val typeSelected: String = listPlaces[position]
                                placeSelected = typeSelected
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {

                            }
                        }
                    }
                }*/
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    //When the camera intent is done, we change the visibility of either the done or error icon
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri: Uri? = null
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                done_icon_front.visibility = View.VISIBLE
            } else{
                error_icon_front.visibility = View.VISIBLE
            }
        }
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                done_icon_back.visibility = View.VISIBLE
            } else{
                error_icon_back.visibility = View.VISIBLE
            }
        }
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
                    Activity.RESULT_OK ->{
                        startTimer()
                        Toasty.success(this@MainActivity, "SMS sent success!", Toasty.LENGTH_SHORT, true).show()
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE ->
                        Toasty.error(this@MainActivity, "No active network to send SMS.", Toasty.LENGTH_SHORT, true).show()
                    SmsManager.RESULT_ERROR_RADIO_OFF ->
                        Toasty.error(this@MainActivity, "SMS not sent 1!", Toasty.LENGTH_SHORT, true).show()
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE ->
                        Toasty.error(this@MainActivity, "SMS not sent 2!", Toasty.LENGTH_SHORT, true).show()
                    SmsManager.RESULT_ERROR_NULL_PDU ->
                        Toasty.error(this@MainActivity, "SMS not sent! 3", Toasty.LENGTH_SHORT, true).show()
                }
            }
        }

        val deliverSMS: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        Toasty.success(this@MainActivity, "SMS delivered.", Toasty.LENGTH_SHORT, true).show()
                    Activity.RESULT_CANCELED ->
                        Toasty.error(this@MainActivity, "SMS not delivered.", Toasty.LENGTH_SHORT, true).show()
                }
            }

        }

        registerReceiver(sendSMS, IntentFilter(SENT))
        registerReceiver(deliverSMS, IntentFilter(DELIVERED))

        smsManager.sendMultipartTextMessage(number,null,parts,sentPI,deliveredPI)
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
}
