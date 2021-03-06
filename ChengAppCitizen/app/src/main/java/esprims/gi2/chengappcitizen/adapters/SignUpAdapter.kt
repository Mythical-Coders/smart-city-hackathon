package esprims.gi2.chengappcitizen.adapters

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SignUpAdapter {
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://cheng-app-auth.herokuapp.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> buildService(service:Class<T>):T{
        return retrofit.create(service)
    }

}