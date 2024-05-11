package com.example.repcgv.api

import com.example.repcgv.models.CinemaSchedule
import com.example.repcgv.models.Schedule
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {
    @GET("schedule/{id}")
    fun GetScheduleByID(@Path("id") id: Int): Call<Schedule>

    @GET("schedule")
    fun GetAllSchedule(): Call<List<Schedule>>


    @POST("schedule/new")
    fun addSchedule(@Body schedule: Schedule): Call<ResponseBody>

    @DELETE("schedule/delete/{id}")
    fun deleteScheduleById(@Path("id") id: Int): Call<ResponseBody>

    @GET("schedule/{id}/tickets")
    fun getScheduleTickets(@Path("id") id: Int): Call<List<String>>

    @GET("schedule/by-date-movie-cinema")
    fun getSchedulesByDateMovieCinema(
        @Query("date") date: String?,
        @Query("movieId") movieId: Int
    ): Call<List<CinemaSchedule>>
}