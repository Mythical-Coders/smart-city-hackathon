package tn.smartCities.chengApp.adapter

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
        .baseUrl("https://9c02e8409612.ngrok.io/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    fun <T> buildService(service:Class<T>):T{
        return retrofit.create(service)
    }
}