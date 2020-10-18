package esprims.gi2.chengappcitizen.adapters

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object PhotoAdapter {
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://cheng-app-upload-photo.herokuapp.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    fun <T> buildService(service:Class<T>):T{
        return retrofit.create(service)
    }
}