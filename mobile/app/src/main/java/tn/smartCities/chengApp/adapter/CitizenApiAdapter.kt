package tn.smartCities.chengApp.adapter

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.smartCities.chengApp.rest.ApiClient

object CitizenApiAdapter {
    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl("https://cheng-app-citizen-data.herokuapp.com")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)
}