package esprims.gi2.chengappcitizen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import esprims.gi2.chengappcitizen.R
import esprims.gi2.chengappcitizen.preference.AppPreference

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)


        //initialize bottom nav bar
        var toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.about_btn -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.home_btn -> {
                    intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
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