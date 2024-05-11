package com.example.repcgv.models

import android.graphics.Color

data class Seat(
    var name: String,
    var id: Int = 0,
    var status: SeatStatus = SeatStatus.None,
    var defaultStatus: SeatStatus = SeatStatus.None,
    var defaultColor: Int = Color.parseColor("#222222"),
    var price: Double = 75000.0,
) {
    companion object {
        val COLOR_BOOKED = Color.parseColor("#dbd7cd")
        val COLOR_CHOOSING = Color.parseColor("#ad2b33")
        val COLOR_VIP = Color.parseColor("#914456")
        val COLOR_NONE = Color.parseColor("#222222")
        val COLOR_NORMAL = Color.parseColor("#aa9c8f")

        enum class SeatStatus(val value: Int, val color: Int, val price: Double) {
            None(0, COLOR_NONE, 0.0),
            Normal(1, COLOR_NORMAL, 75000.0),
            VIP(2, COLOR_VIP, 120000.0),
            Booked(3, COLOR_BOOKED, 0.0),
            Choosing(4, COLOR_CHOOSING, 0.0);

            companion object {
                fun fromInt(value: Int) = values().first { it.value == value }
            }
        }
    }
}