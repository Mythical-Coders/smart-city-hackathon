package esprims.gi2.chengappcitizen.rest

import esprims.gi2.chengappcitizen.models.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {
    @Headers("Content-Type: application/json")

    //call signUp Service
    @POST("/api/auth/signup")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    //call profile-citizen Service
    @POST("/api/citizen/")
    fun addProfile(@Body profileRequest: ProfileRequest):Call<ProfileResponse>

    //call signIn service
    @POST("/api/auth/signin")
    fun signIn(@Body signInRequest: SignInRequest):Call<SignInResponse>

    //call Report service
    @POST("/api/report/")
    fun addReport(@Body reportRequest: ReportRequest):Call<ReportResponse>

    //call Upload_photo Service
    @Multipart
    @POST ("api/photos/add")
    fun addPhoto(@Part("imageName") imageName:String,@Part imageFile:MultipartBody.Part):Call<String>

    //call Map Service
    @POST("/api/reportPlace/")
    fun addPlace(@Body mapRequest: MapRequest):Call<MapResponse>



}