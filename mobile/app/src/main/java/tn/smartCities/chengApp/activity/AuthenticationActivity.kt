package tn.smartCities.chengApp.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.smartCities.chengApp.R
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.smartCities.chengApp.adapter.AuthApiAdapter
import tn.smartCities.chengApp.model.User
import tn.smartCities.chengApp.model.UserCredentials
import tn.smartCities.chengApp.preference.AppPreferences
import tn.smartCities.chengApp.rest.ApiClient

class AuthenticationActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري تسجيل الدخول ...")
        progressDialog.setCancelable(true)

        var mIsShowPass = false

        show_pass.setOnClickListener {
            mIsShowPass = !mIsShowPass
            showPassword(mIsShowPass)
        }

        showPassword(mIsShowPass)

        loginId.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                loginId.setBackgroundResource(R.drawable.edit_round_focus)
            }else{
                loginId.setBackgroundResource(R.drawable.edit_round)
            }
        }

        passId.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                passId.setBackgroundResource(R.drawable.edit_round_focus)
            }else{
                passId.setBackgroundResource(R.drawable.edit_round)
            }
        }

        SigninButton.setOnClickListener {
            val username = loginId.text.toString()
            val pwd = passId.text.toString()
            val userCredential = UserCredentials(username, pwd)

            launch(Dispatchers.Main){
                try{
                    progressDialog.show()
                    signIn(userCredential){
                       if (it != null) {
                           Toast.makeText(applicationContext, "تم تسجيل الدخول بنجاح",Toast.LENGTH_LONG).show()
                           if (username.isNotBlank() && pwd.isNotBlank()) {
                               AppPreferences.isLogin = true
                               AppPreferences.username = username
                               AppPreferences.password = pwd
                               AppPreferences.id = it.id
                           }
                           intent = Intent(applicationContext, MainActivity::class.java)
                           progressDialog.dismiss()
                           startActivity(intent)
                       } else {
                           Toast.makeText(applicationContext, "الرجاء التثبت من المعطيات",Toast.LENGTH_LONG).show()
                           passId.setBackgroundResource(R.drawable.edit_round_false)
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

    private fun signIn(userData: UserCredentials, onResult: (User?) -> Unit){
        val retrofit = AuthApiAdapter.buildService(ApiClient::class.java)
        retrofit.signin(userData).enqueue(
            object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<User>, response: Response<User>) {
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}



