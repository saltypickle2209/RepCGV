package com.example.repcgv.models

import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("food_id")
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    @SerializedName("image_path")
    val poster: String,
)