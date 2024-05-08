package com.example.repcgv.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: String,
    val googleId: String?,
    @SerializedName("name") val name: String?,
    val avatar: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("email") val email: String,
    val password: String?,
    @SerializedName("address") val address: List<String>?,
    @SerializedName("dob") val dob: String?,
    @SerializedName("role") val role: String?,
    val createdAt: String?,
    val isBanned: Boolean?,
    val emailVerified: Boolean?,
    val emailVerificationToken: String?,
    val emailVerificationExpires: Long?
)
