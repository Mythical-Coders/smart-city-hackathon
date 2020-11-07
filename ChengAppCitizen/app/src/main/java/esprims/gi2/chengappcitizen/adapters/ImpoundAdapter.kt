package esprims.gi2.chengappcitizen.adapters

import com.google.gson.GsonBuilder
import esprims.gi2.chengappcitizen.rest.ApiClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ImpoundAdapter {

    /*private val client = OkHttpClient()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://cheng-app.herokuapp.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> buildService(service:Class<T>):T {
       return retrofit.create(service)
    }*/
    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl("https://cheng-app.herokuapp.com/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)
}