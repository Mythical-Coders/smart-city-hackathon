package esprims.gi2.chengappcitizen.activities

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import esprims.gi2.chengappcitizen.R
import esprims.gi2.chengappcitizen.adapters.SignInAdapter
import esprims.gi2.chengappcitizen.models.SignInRequest
import esprims.gi2.chengappcitizen.models.SignInResponse
import esprims.gi2.chengappcitizen.preference.AppPreference
import esprims.gi2.chengappcitizen.rest.ApiClient
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class WelcomeActivity : AppCompatActivity(), CoroutineScope by MainScope() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_welcome)


        // load animation
        val topAnim: Animation = AnimationUtils.loadAnimation(this,R.anim.top_animation)
        val botAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        //set animation
        appLogo.animation = topAnim
        logoBackground.animation = botAnim
        appName.animation = topAnim
        signUp_btn.animation = botAnim
        signIn_btn.animation = topAnim



        //show or hide password
        var mIsShowPass = false

        show_pass.setOnClickListener {
            mIsShowPass = !mIsShowPass
            showPassword(mIsShowPass)
        }
        showPassword(mIsShowPass)


        //fill the username field if the user completed signing up
        if (intent.getStringExtra("username2")!=null){
            val username2 = intent.getStringExtra("username2")
            loginId.setText(username2)
            Toast.makeText(applicationContext, "الرجاء إدخال كلمة السر",Toast.LENGTH_LONG).show()
        }


        AppPreference.init(this)

        //test if user has logged before if so --> direct to Main activity
        if (AppPreference.isLogin){

            intent =Intent(applicationContext,MainActivity::class.java)
            intent.putExtra("id",AppPreference.id)
            startActivity(intent)
        }

        //launch signUp Activity
        signUp_btn.setOnClickListener{
            intent=Intent(applicationContext,RegisterActivity::class.java)
            startActivity(intent)
        }




        //Log in to application
        signIn_btn.setOnClickListener {

            val username = loginId.text.toString()
            val password = passId.text.toString()

            val signInRequest = SignInRequest(username,password)
            launch(Dispatchers.Main){
                try {
                    signIn(signInRequest){
                        if (it != null) {

                            Toast.makeText(applicationContext, "تم تسجيل الدخول بنجاح",Toast.LENGTH_LONG).show()

                            if (username.isNotBlank() && password.isNotBlank()) {
                                AppPreference.isLogin = true
                                AppPreference.username = username
                                AppPreference.password = password
                                AppPreference.id = it.id
                            }
                            intent = Intent(applicationContext, MainActivity::class.java)
                            intent.putExtra("idFirstLogIn",username)
                            intent.putExtra("id",it.id)
                            startActivity(intent)

                        } else {

                            Toast.makeText(applicationContext, "الرجاء التثبت من المعطيات",Toast.LENGTH_LONG).show()

                        }
                    }

                }catch(e: Exception){
                    Toast.makeText(applicationContext,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }

    }


  /*  override fun OnActivityResult(requestCode: Int, resultCode: Int,data:Intent?){

        super.onActivityResult(requestCode,resultCode,data)
        when(requestCode){
            1->
                when(resultCode){
                    Activity.RESULT_OK ->{
                        val username= data?.getStringExtra("username")
                        loginId.setText(username)
                        Toast.makeText(applicationContext, "الرجاء إدخال كلمة السر",Toast.LENGTH_LONG).show()

                    }
                    Activity.RESULT_CANCELED ->  Toast.makeText(applicationContext, "Action Annulé",Toast.LENGTH_LONG).show()

                }
        }
    }*/
    private fun signIn(userData: SignInRequest, onResult: (SignInResponse?) -> Unit){
        val retrofit = SignInAdapter.buildService(ApiClient::class.java)
        retrofit.signIn(userData).enqueue(
            object : retrofit2.Callback<SignInResponse> {
                override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<SignInResponse>,
                    response: Response<SignInResponse>
                ) {
                    val signedUser = response.body()
                    onResult(signedUser)
                }


            }
        )
    }

    private fun showPassword(isShow: Boolean){
        if(isShow){
            passId.transformationMethod = HideReturnsTransformationMethod.getInstance()
            show_pass.setImageResource(R.drawable.hide_pass)
        }else{
            passId.transformationMethod = PasswordTransformationMethod.getInstance()
            show_pass.setImageResource(R.drawable.show_pass)
        }
        passId.setSelection(passId.text.toString().length)
    }


    }
