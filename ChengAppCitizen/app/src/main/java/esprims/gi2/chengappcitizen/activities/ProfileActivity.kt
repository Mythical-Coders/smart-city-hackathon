package esprims.gi2.chengappcitizen.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import esprims.gi2.chengappcitizen.R
import esprims.gi2.chengappcitizen.adapters.ProfileAdapter
import esprims.gi2.chengappcitizen.models.ProfileRequest
import esprims.gi2.chengappcitizen.models.ProfileResponse
import esprims.gi2.chengappcitizen.rest.ApiClient
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ProfileActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val username = intent.getStringExtra("username")

        addProfile.setOnClickListener {
            val cin = cinId.text.toString()
            val phone = phoneId.text.toString()
            val matricule = matriculeId.text.toString()


            launch(Dispatchers.Main){
                try {
                    val profileRequest = ProfileRequest(matricule,cin,phone)

                    addProfile(profileRequest){
                        if(it!=null){

                                Toast.makeText(
                                    applicationContext,
                                    "تم تحديث الحساب بنجاح",
                                    Toast.LENGTH_LONG
                                ).show()

                                intent = Intent(applicationContext,WelcomeActivity::class.java)
                                intent.putExtra("username2",username)
                                startActivity(intent)
                        }
                        else{
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
    private fun addProfile(profileRequest: ProfileRequest,onResult:(ProfileResponse?)->Unit){
        val retrofit = ProfileAdapter.buildService(ApiClient::class.java)
        retrofit.addProfile(profileRequest).enqueue(
            object : retrofit2.Callback<ProfileResponse>{
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    val addToProfile = response.body()
                    onResult(addToProfile)
                }

            }
        )
    }


}