package com.example.repcgv.api

import com.example.repcgv.models.Order
import com.example.repcgv.models.Ticket
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface OrderApi {
    @POST("order/new")
    fun uploadOrder(
        @Body order: Order
    ): Call<ResponseBody>

    @GET("order/{id}")
    fun getOrderById(@Path("id") id: Int): Call<Order>
    @POST("order/update")
    fun updateOrderById(@Header("Authorization") authToken: String,@Body request: JsonObject ): Call<Order>

    @GET("order/detailedOrder/unused")
    fun getAllUnusedOrder(@Header("Authorization") authToken: String): Call<List<Ticket>>

    @GET("order/detailedOrder/used")
    fun getAllUsedOrder(@Header("Authorization") authToken: String): Call<List<Ticket>>

    @GET("order/detailedOrder/{id}")
    fun getDetailedOrderById(@Path("id") id: Int): Call<Ticket>

}