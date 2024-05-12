package com.example.repcgv.api

import com.example.repcgv.models.Cinema
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CinemaApi {
    @GET("cinemas")
    fun getAllCinemas(): Call<List<Cinema>>

    @GET("cinemas/{id}")
    fun getCinemaById(@Path("id") id: Int): Call<Cinema>
}