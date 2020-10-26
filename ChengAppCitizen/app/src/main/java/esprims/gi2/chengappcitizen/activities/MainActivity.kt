package esprims.gi2.chengappcitizen.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import esprims.gi2.chengappcitizen.R
import esprims.gi2.chengappcitizen.classes.Photo
import esprims.gi2.chengappcitizen.classes.PhotoManager
import esprims.gi2.chengappcitizen.classes.ReportListAdapter
import esprims.gi2.chengappcitizen.preference.AppPreference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()  {

    private val photoManager: PhotoManager = PhotoManager(this)
    private var list : ArrayList<Photo> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        setContentView(R.layout.activity_main)


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
                else ->{
                    return@OnNavigationItemSelectedListener true
                }
            }
        }


}