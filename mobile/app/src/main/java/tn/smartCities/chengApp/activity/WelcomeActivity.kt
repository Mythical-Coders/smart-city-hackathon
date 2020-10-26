package tn.smartCities.chengApp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.smartCities.chengApp.R
import com.smartCities.chengApp.R.anim.*
import kotlinx.android.synthetic.main.activity_welcome.*
import tn.smartCities.chengApp.preference.AppPreferences

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreferences.init(this)
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)

        val topAnim: Animation = AnimationUtils.loadAnimation(this, top_animation)
        val botAnim: Animation = AnimationUtils.loadAnimation(this, bottom_animation)

        appLogo.animation = topAnim
        logoBackground.animation = topAnim
        appName.animation = botAnim
        logIn_btn.animation = botAnim
        exit_btn.animation = botAnim

        if (AppPreferences.isLogin) {
            welcomeText.text = getString(R.string.welcome_text,AppPreferences.username)
            logIn_btn.visibility = GONE
            exit_btn.visibility = GONE
            Handler().postDelayed({
                intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }, 2000)
        } else {
            welcomeText.visibility = GONE
            logIn_btn.visibility = VISIBLE
            exit_btn.visibility = VISIBLE
        }

        logIn_btn.setOnClickListener {
            intent = Intent(applicationContext, AuthenticationActivity::class.java)
            startActivity(intent)
        }

        exit_btn.setOnClickListener {
            finishAffinity()
        }
    }
}


