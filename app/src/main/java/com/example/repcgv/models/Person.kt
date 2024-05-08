package com.example.repcgv.models

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
{}