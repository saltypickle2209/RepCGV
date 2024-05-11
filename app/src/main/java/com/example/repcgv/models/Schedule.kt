package com.example.repcgv.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Schedule(
    @SerializedName("schedule_id") val scheduleId: Int,
    @SerializedName("movie_id") val movieId: Int,
    @SerializedName("room_id") val roomId: Int,
    @SerializedName("cinema_id") val cinemaId: Int,
    @SerializedName("schedule_date") val scheduleDate: Long,
    @SerializedName("schedule_start") val scheduleStart: String,
    @SerializedName("booked_seats") val seats: List<Int>,
) : Serializable