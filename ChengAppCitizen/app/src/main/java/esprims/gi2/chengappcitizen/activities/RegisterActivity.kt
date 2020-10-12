package esprims.gi2.chengappcitizen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import esprims.gi2.chengappcitizen.R
import esprims.gi2.chengappcitizen.adapters.SignUpAdapter
import esprims.gi2.chengappcitizen.models.SignUpRequest
import esprims.gi2.chengappcitizen.models.SignUpResponse
import esprims.gi2.chengappcitizen.preference.AppPreference
import esprims.gi2.chengappcitizen.rest.ApiClient
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import javax.xml.validation.Validator

class RegisterActivity : AppCompatActivity(), CoroutineScope by MainScope(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val roles: List<String> = listOf("ROLE_CITIZEN")

        //show or hide password
        var mIsShowPass1 = false
        var mIsShowPass2 = false

        //password field
        show_pass1.setOnClickListener {
            mIsShowPass1 = !mIsShowPass1
            showPassword(mIsShowPass1)
        }

        //repeat password field
        show_pass2.setOnClickListener {
            mIsShowPass2 = !mIsShowPass2
            showPassword(mIsShowPass2)
        }

        //




        signUpComfirm_btn.setOnClickListener {

            //declare variables
            val username = signUpNameId.text.toString()
            val email = signUpEmailId.text.toString()
            val password = signUpPassId.text.toString()
            val repeatPassword = repeatPassId.text.toString()

            launch(Dispatchers.Main) {
                try {

                    val signUpRequest = SignUpRequest(username,email,password,roles)

                    signUp(signUpRequest) {

                        if (it != null) {

                            if (accept_terms_and_conditions.isChecked ) {

                                if (password == repeatPassword) {

                                    when (it.message) {
                                        "Email is already in use!" -> {
                                            Toast.makeText(
                                                applicationContext,
                                                "بريد إلكتروني مسجل بقاعدة البينات",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        "Username is already taken!" -> {
                                            Toast.makeText(
                                                applicationContext,
                                                "إسم مسجل بقاعدة البينات",
                                                Toast.LENGTH_LONG
                                            ).show()

                                        }
                                        "User registered successfully!" -> {
                                            Toast.makeText(
                                                applicationContext,
                                                "تم تسجيل الدخول بنجاح",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            intent =
                                                Intent(
                                                    applicationContext,
                                                    ProfileActivity::class.java
                                                )
                                            intent.putExtra("username",username)
                                            startActivity(intent)
                                        }

                                    }
                                }
                                else{
                                    Toast.makeText(
                                        applicationContext,
                                        "كلمات السر غير متقابلين",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            else {
                                Toast.makeText(
                                    applicationContext,
                                    "الرجاء الموافقة على أحكام و شروط التطبيق",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }

                        else{
                            Toast.makeText(
                                applicationContext,
                                "الرجاء التثبت من المعطيات",
                                Toast.LENGTH_LONG
                            ).show()
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
    private fun showPassword(isShow:Boolean){
        if(isShow){
            signUpPassId.transformationMethod = HideReturnsTransformationMethod.getInstance()
            show_pass.setImageResource(R.drawable.hide_pass)
        }
        else{
            signUpPassId.transformationMethod =PasswordTransformationMethod.getInstance()
            show_pass.setImageResource(R.drawable.show_pass)
        }
        signUpPassId.setSelection(signUpPassId.text.toString().length   )
    }

    private fun signUp(signUpRequest: SignUpRequest, onResult: (SignUpResponse?)->Unit){
        val retrofit = SignUpAdapter.buildService(ApiClient::class.java)
        retrofit.signUp(signUpRequest).enqueue(
            object :retrofit2.Callback<SignUpResponse> {
                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {

                    onResult(null)
                }

                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {

                    val profileSignUp = response.body()
                    onResult(profileSignUp)
                }

            }
        )
    }
}