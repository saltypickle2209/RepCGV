package com.example.repcgv.models

data class CinemaSchedule(
    val id: String,
    val cinemaId: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val schedules: List<Schedule>
)