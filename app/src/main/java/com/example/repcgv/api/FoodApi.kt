package com.example.repcgv.api

import com.example.repcgv.models.Food
import com.example.repcgv.models.FoodPayment
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {
    @GET("food")
    fun getAllFood(): Call<List<Food>>
    @GET("food/byOrder")
    fun getFoodByOrder(@Query("orderId") orderId: Int): Call <List<FoodPayment>>
    @GET("food/{id}")
    fun getFoodById(@Path("id") id: Int): Call<Food>
}