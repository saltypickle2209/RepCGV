package com.example.repcgv.models

import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("order_id") val id: Int,
    @SerializedName("movie_title") val movieName: String,
    @SerializedName("movie_poster") val moviePoster: String,
    @SerializedName("movie_classification") val movieClassification: String,
    @SerializedName("room_id") val roomId: Int,
    @SerializedName("cinema_name") val cinemaName: String,
    @SerializedName("cinema_address") val cinemaAddress: String,
    @SerializedName("schedule_date") val scheduleDate: Long,
    @SerializedName("schedule_start") val scheduleStart: String,
    @SerializedName("ticket") val ticket: List<String>,
    @SerializedName("discount") val discount: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("total_ticket") val totalTicket: Int,
    @SerializedName("total_food") val totalFood: Int,
    @SerializedName("payment_method") val paymentMethod: String
)