package tn.smartCities.chengApp.rest

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import tn.smartCities.chengApp.model.Citizen
import tn.smartCities.chengApp.model.Impound
import tn.smartCities.chengApp.model.User
import tn.smartCities.chengApp.model.UserCredentials

interface ApiClient {
    @Headers("Content-Type: application/json")
    @POST("/api/auth/signin")
    fun signin(@Body userCredentials: UserCredentials):Call<User>

    @GET("/api/citizen/matricule/{matricule}")
    suspend fun getCitizenByMatricule(@Path("matricule") matricule: String): Response<Citizen>

    @Headers("Content-Type: application/json")
    @POST("/api/impound/")
    fun addImpound(@Body impound: Impound):Call<Impound>
}