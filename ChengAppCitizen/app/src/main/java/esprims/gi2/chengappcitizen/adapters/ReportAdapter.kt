package esprims.gi2.chengappcitizen.adapters

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ReportAdapter {
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("") //to add in the next commit
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> buildService(service:Class<T>):T{
        return retrofit.create(service)
    }
}