package com.example.repcgv.api

import com.example.repcgv.models.Movie
import com.example.repcgv.models.Person
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movies/currently-showing") // Đường dẫn tới API
    fun getCurrentlyShowingMovies(): Call<List<Movie>>

    @GET("movies/{id}")
    fun getMovieByID(@Path("id") id: Int): Call<Movie>

    @GET("movies")
    fun getAllMovies(): Call<List<Movie>>

    @GET("movies/{id}/persons-involved")
    fun getAllPersonInvolved(@Path("id") id: Int): Call<List<Person>>

    @GET("movies/search")
    fun getMoviesByName(@Query("keyword") keyword: String, @Query("currently_showing") currentlyShowing: Boolean): Call<List<Movie>>

    @Multipart
    @POST("movies/uploadMovie")
    fun uploadMovie(
        @Part poster: MultipartBody.Part?,
        @Part("id") id: RequestBody?,
        @Part("backdrop_path") backdropPath: RequestBody,
        @Part("genre_ids") genreIds: RequestBody,
        @Part("title") title: RequestBody,
        @Part("overview") overview: RequestBody,
        @Part("vote_average") voteAverage: RequestBody,
        @Part("actors") actors: RequestBody,
        @Part("director") director: RequestBody,
        @Part("runTime") runTime: RequestBody,
        @Part("classification") classification: RequestBody
    ): Call<ResponseBody>

    @DELETE("movies/delete/{id}")
    fun deleteMovieByID(@Path("id") id: Int): Call<ResponseBody>

//    @GET("/fact")
//    fun getCatFact(): Call<CatFactResponse>
}