package esprims.gi2.chengappcitizen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import esprims.gi2.chengappcitizen.R

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        report_btn.setOnClickListener{

            //load id user
            if(intent.getStringExtra("idFirstLogIn")!=null){
                val id = intent.getStringExtra("idFirstLogIn")
                intent = Intent(applicationContext,ReportActivity::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
            }

            else if (intent.getStringExtra("id")!=null){
                val id = intent.getStringExtra("id")
                intent = Intent(applicationContext,ReportActivity::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
            }





        }
    }

}