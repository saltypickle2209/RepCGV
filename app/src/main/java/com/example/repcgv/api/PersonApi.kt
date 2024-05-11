package com.example.repcgv.api

import com.example.repcgv.models.Person
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonApi {
    @GET("person")
    fun getAllPeople(): Call<List<Person>>

    @GET("person/{id}")
    fun getPersonByID(@Path("id") id: Int): Call<Person>
}