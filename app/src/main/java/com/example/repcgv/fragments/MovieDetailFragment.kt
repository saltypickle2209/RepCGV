package com.example.repcgv.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.repcgv.MainActivity
import com.example.repcgv.R
//import com.example.repcgv.api.AccountApi
//import com.example.repcgv.api.AuthService
//import com.example.repcgv.api.GenreApi
import com.example.repcgv.api.MovieApi
//import com.example.repcgv.api.PersonApi
//import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.models.AuthResponse
import com.example.repcgv.models.Genre
import com.example.repcgv.models.Movie
import com.example.repcgv.models.Person
import com.example.repcgv.models.Review
import com.example.repcgv.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MovieDetailFragment : Fragment() {
    private lateinit var root: View;

    private lateinit var movie: Movie

    private lateinit var btnBook : Button
    private lateinit var btnReview: AppCompatButton


    private lateinit var imageBackdrop: ImageView
    private lateinit var imagePoster: ImageView

    private lateinit var textMovieName: TextView
    private lateinit var textDate: TextView
    private lateinit var textTime: TextView
    private lateinit var textGenre: TextView
    private lateinit var textRated: TextView
    private lateinit var textDescription: TextView
    private lateinit var textCast: TextView
    private lateinit var textDirector: TextView

    private var movieID = 438631
    private lateinit var people: List<Person>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        arguments?.takeIf { it.containsKey("id") }?.apply {
            movieID = getInt("id")
        }

        fetchData()
        init()
        return  root
    }

    private fun fetchData()
    {
//        val movieService = RetrofitClient.instance.create(MovieApi::class.java)
//        val call = movieService.getMovieByID(movieID)
//        call.enqueue(object : Callback<Movie> {
//            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
//                if (response.isSuccessful) {
//                    // Handle successful response
//                    movie = response.body()!!
//                    Log.i("API", movie.name)
//                    fetchPeopleData()
//                } else {
//                    val errorMessage = response.message()
//                    Log.i("API", errorMessage)
//                    Log.i("API", "GET FAILED")
//                }
//            }
//
//            override fun onFailure(call: Call<Movie>, t: Throwable) {
//                Log.i("API", t.message!!)
//            }
//        })
    }

    private fun fetchPeopleData(){
//        val movieService = RetrofitClient.instance.create(MovieApi::class.java)
//        val call = movieService.getAllPersonInvolved(movie.id)
//        call.enqueue(object : Callback<List<Person>> {
//            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
//                if (response.isSuccessful) {
//                    people = response.body()!!
//                    setValue()
//                } else {
//                    val errorMessage = response.message()
//                    Log.i("API", errorMessage)
//                    Log.i("API", "GET FAILED")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
//                Log.i("API", t.message!!)
//            }
//        })
    }

    private fun init()
    {
        btnBook = root.findViewById(R.id.buttonBook)
        btnReview = root.findViewById(R.id.buttonReview)

        imageBackdrop = root.findViewById(R.id.imageBackdrop)
        imagePoster = root.findViewById(R.id.imagePoster)

        textMovieName = root.findViewById(R.id.textMovieName)
        textDate = root.findViewById(R.id.textDate)
        textTime = root.findViewById(R.id.textTime)
        textDescription = root.findViewById(R.id.textDescription)
        textCast = root.findViewById(R.id.textCast)
        textDirector = root.findViewById(R.id.textDirector)
        textRated = root.findViewById(R.id.textRated)
        textGenre = root.findViewById(R.id.textGenre)

        btnBook.setOnClickListener {
            tryOrder()
        }

        btnReview.setOnClickListener{
//            (this.activity as? MainActivity)?.addFragment(MovieReviewFragment(), "reviewList")
        }
    }

    private fun setValue()
    {
        val castlist = people.subList(1, people.size)
        val namesString = castlist.joinToString(separator = ", ") { it.name }

        textMovieName.text = movie.name;
        textDate.text = extractDate(movie.premiereDate);
        textTime.text = formatTime((movie.duration))
        textDirector.text = people[0].name
        textCast.text = namesString
        textDescription.text = movie.overview
        textRated.text = movie.classification

        getGenresNamesByIds(movie.genreIds) { namesString ->
            // Set the text of textGenre.text with the resulting names string
            textGenre.text = namesString
        }


        Glide.with(this).load( "https://image.tmdb.org/t/p/original" + movie.poster).into(imagePoster)
        Glide.with(this).load( "https://image.tmdb.org/t/p/original" + movie.backdropPath).into(imageBackdrop)
    }

    fun getGenresNamesByIds(ids: List<Int>, callback: (String) -> Unit) {
        val namesList = mutableListOf<String>()
//        val genreApi = RetrofitClient.instance.create(GenreApi::class.java)
//        // Iterate through each ID and fetch the corresponding genre's name
//        ids.forEach { id ->
//            genreApi.getGenreByID(id).enqueue(object : Callback<Genre> {
//                override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
//                    if (response.isSuccessful) {
//                        val genre = response.body()
//                        if (genre != null) {
//                            namesList.add(genre.name)
//                        }
//                    }
//                    // Check if all names have been fetched
//                    if (namesList.size == ids.size) {
//                        // Concatenate the names into a single string separated by commas
//                        val namesString = namesList.joinToString(", ")
//                        callback(namesString)
//                    }
//                }
//
//                override fun onFailure(call: Call<Genre>, t: Throwable) {
//                }
//            })
//        }
    }

    private fun formatTime(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        return if (hours == 0) {
            "$remainingMinutes mins"
        } else if (remainingMinutes == 0) {
            "$hours hrs"
        } else {
            "$hours hrs $remainingMinutes mins"
        }
    }
    private fun extractDate(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateTimeString)
        return outputFormat.format(date)
    }

    private fun tryOrder(){
        val sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""

//        if (token != "") {
//            val accountService = RetrofitClient.instance.create(AccountApi::class.java)
//            val call = accountService.getUserDetail(token)
//            call.enqueue(object : Callback<User> {
//                override fun onResponse(call: Call<User>, response: Response<User>) {
//                    if (response.isSuccessful) {
//                        val args = Bundle()
//                        val user = response.body()!!
//                        args.putInt("movieId",movieID)
//                        val fragment = BookingTimeFragment()
//                        fragment.arguments = args
//                        (this@MovieDetailFragment.activity as? MainActivity)?.addFragment(fragment,"bookingTime")
//
//                    } else {
//                        (this@MovieDetailFragment.activity as? MainActivity)?.addFragment(LoginFragment(), "login")
//                    }
//                }
//
//                override fun onFailure(call: Call<User>, t: Throwable) {
//                    Log.i("API", t.message!!)
//                }
//            })
//        }else{
//            (this@MovieDetailFragment.activity as? MainActivity)?.addFragment(LoginFragment(), "login")
//        }
    }
}

