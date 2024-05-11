package com.example.repcgv.api

import com.example.repcgv.models.User
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountApi {
    @GET("account")
    fun getUserDetail(@Header("Authorization") authToken: String): Call<User>

    @POST("account/update")
    fun updateUserDetail(@Header("Authorization") authToken: String, @Body request: JsonObject): Call<ResponseBody>

    @POST("account/password/update")
    fun changeUserPassword(@Header("Authorization") authToken: String, @Body request: JsonObject): Call<ResponseBody>
}