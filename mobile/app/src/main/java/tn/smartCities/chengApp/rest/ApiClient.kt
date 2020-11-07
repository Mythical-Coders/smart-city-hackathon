package tn.smartCities.chengApp.rest

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Multipart
import tn.smartCities.chengApp.model.*

interface ApiClient {
    @Headers("Content-Type: application/json")
    @POST("/api/auth/signin")
    fun signin(@Body userCredentials: UserCredentials):Call<User>

    @GET("/api/citizen/matricule/{matricule}")
    suspend fun getCitizenByMatricule(@Path("matricule") matricule: String): Response<Citizen>

    @Headers("Content-Type: application/json")
    @POST("/api/impound/")
    fun addImpound(@Body impound: Impound):Call<Impound>

    @GET("/api/place/all")
    suspend fun getPlaces(): Response<List<PlaceResponse>>

    //@Headers("Content-Type: multipart/form-data")
    @Multipart
    @POST ("/predict")
    fun addPhoto(@Part imageFile:MultipartBody.Part):Call<PhotoResponse>
}