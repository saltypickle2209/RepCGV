package com.example.repcgv.api

import com.example.repcgv.models.Room
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RoomApi {
    @GET("room/{id}")
    fun GetRoomById(@Path("id") id: Int): Call<Room>

    @GET("room")
    fun GetAllRoom(): Call<List<Room>>
}