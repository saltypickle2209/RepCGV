package com.example.repcgv.models

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("order_id") val orderId: Int,
    @SerializedName("user_id") val userId: String,
    @SerializedName("schedule_id") val scheduleId: Int,
    @SerializedName("time") val time: Long,
    @SerializedName("tickets") val tickets: List<String>,
    @SerializedName("food") val food: List<Int>,
    @SerializedName("discount") val discount: Double,
    @SerializedName("total") val total: Double,
    @SerializedName("total_ticket") val totalTicket: Double,
    @SerializedName("total_food") val totalFood: Double,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("status") val status: String,
)