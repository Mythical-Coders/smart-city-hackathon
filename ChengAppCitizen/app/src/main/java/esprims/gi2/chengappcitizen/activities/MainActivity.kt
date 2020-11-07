package esprims.gi2.chengappcitizen.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.bottomnavigation.BottomNavigationView
import esprims.gi2.chengappcitizen.R
import esprims.gi2.chengappcitizen.adapters.ImpoundAdapter
import esprims.gi2.chengappcitizen.classes.Photo
import esprims.gi2.chengappcitizen.classes.PhotoManager
import esprims.gi2.chengappcitizen.classes.ReportListAdapter
import esprims.gi2.chengappcitizen.models.ImpoundResponse
import esprims.gi2.chengappcitizen.preference.AppPreference
import esprims.gi2.chengappcitizen.rest.ApiClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.await
import java.lang.Runnable
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope()  {

    //light SQL database manager
    private val photoManager: PhotoManager = PhotoManager(this)
    private var list : ArrayList<Photo> = ArrayList()

    //notification manager
    companion object {
        private const val NOTIFICATION_ID = 112
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }
    private lateinit var notificationManager: NotificationManager
    private lateinit var builder : Notification.Builder


    //variables for handler
    var mHandler: Handler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_main)



        //refresh activity
        mHandler = Handler()
        m_Runnable.run()


        //initialize bottom nav bar
        var toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)



        //fill the list from database
        list = this.refresh()

        report_btn.setOnClickListener{

            intent = Intent(applicationContext,ReportActivity::class.java)
            startActivity(intent)


        }

    }
    private fun refresh(): ArrayList<Photo> {

        photoManager.openReadDB()
        list= photoManager.getPhoto()

        reportList.adapter = ReportListAdapter(applicationContext,list)
        //list_id.adapter = ArrayAdapter<Photo>(this,android.R.layout.simple_list_item_1,list)

        photoManager.closeDB()
        return list

    }
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.about_btn -> {

                    intent = Intent(applicationContext,AboutUsActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.home_btn -> {

                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout_btn -> {
                    intent = Intent(applicationContext,WelcomeActivity::class.java)
                    AppPreference.isLogin = false
                    AppPreference.password = ""
                    AppPreference.username = ""
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.notif_btn ->{
                    intent = Intent(applicationContext,ImpoundActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                else ->{
                    return@OnNavigationItemSelectedListener true
                }
            }
        }



    /*private fun getImpoundService(id:String, onresult: (List<ImpoundResponse>) -> Unit){
        val retrofit = ImpoundAdapter.buildService(ApiClient::class.java)
        retrofit.getImpound(id).enqueue(
            object : retrofit2.Callback<List<ImpoundResponse>>{
                override fun onFailure(call: Call<List<ImpoundResponse>>, t: Throwable) {
                    onresult(emptyList())
                }

                override fun onResponse(
                    call: Call<List<ImpoundResponse>>,
                    response: Response<List<ImpoundResponse>>
                ) {
                    response.body()?.let { onresult(it) }
                }

            }
        )
    }*/
    private fun createNotification() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // launch new intent after clicking on the notification
        val intent = Intent(applicationContext,ImpoundActivity::class.java)

        //convert to pending intent
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Messages",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableLights(true)
            channel.lightColor = Color.YELLOW
            channel.enableVibration(true)
            channel.description = "Your car has been impounded!"
            notificationManager.createNotificationChannel(channel)

            builder = Notification.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("impound Notification")
                .setContentTitle("Your car has been impounded !")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(123,builder.build())
    }

    private val m_Runnable: Runnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            //get user's id
            val id = AppPreference.id


            //the list of all impounds for 1 citizen
            var impoundList :List<ImpoundResponse>
            launch(Dispatchers.Main){

               try {
                   val response = ImpoundAdapter.apiClient.getImpound(id)

                       if(response.isExecuted){
                          impoundList =  response.await()
                           val lastIndex = impoundList.lastIndex
                           val lastImpound = impoundList[lastIndex]
                           val dateService = lastImpound.date
                           val dateServiceInstant = dateService.toInstant()
                           val currentDay = ZonedDateTime.now().toInstant()
                           //if (currentDay > dateServiceInstant)

                           Toast.makeText(this@MainActivity, "date: $dateService", Toast.LENGTH_LONG).show()

                           //update the notif button
                           val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
                           bottomNavigation[3].isEnabled = true
                           val badge = bottomNavigation.getOrCreateBadge(R.id.notif_btn)
                           badge.isVisible = true
                           badge.number = 1

                           //show notification
                           createNotification()

                       }

                        /*else{
                           Toast.makeText(this@MainActivity, "id: $id", Toast.LENGTH_LONG).show()
                       }*/

                  /* getImpoundService(id){
                       if (it.isNotEmpty()){
                           val lastIndex = it.lastIndex
                           val lastImpound = it[lastIndex]
                           val dateService = lastImpound.date
                           val dateServiceInstant = dateService.toInstant()
                           val currentDay = ZonedDateTime.now().toInstant()
                           //if (currentDay > dateServiceInstant)

                           Toast.makeText(this@MainActivity, "date: $dateService", Toast.LENGTH_LONG).show()

                           //update the notif button
                           val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
                           bottomNavigation[3].isEnabled = true
                           val badge = bottomNavigation.getOrCreateBadge(R.id.notif_btn)
                           badge.isVisible = true
                           badge.number = 1

                           //show notification
                           createNotification()
                       }
                       else{
                           Toast.makeText(this@MainActivity, "id: $id", Toast.LENGTH_LONG).show()

                       }

                   }*/

               }catch(e: Exception){
                   Toast.makeText(applicationContext,
                       "Error Occurred: ${e.message}",
                       Toast.LENGTH_LONG).show()
               }
           }
            //update the get service every 50 seconds
            this@MainActivity.mHandler?.postDelayed(this, 50000)
        }
    }



}