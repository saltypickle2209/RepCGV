package com.example.repcgv.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.api.MovieApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.adapters.RecyclerViewMovieAdapter
import com.example.repcgv.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookByMovieFragment : Fragment() {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var recyclerViewMovieList: RecyclerView
    private lateinit var recyclerViewMovieListAdapter: RecyclerViewMovieAdapter

    private lateinit var movieList: ArrayList<Movie>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_by_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData(view)
        init(view)
    }

    private fun fetchData(view: View){
        val movieService = RetrofitClient.instance.create(MovieApi::class.java)
        val call = movieService.getCurrentlyShowingMovies()

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    movieList = ArrayList(response.body()!!)
                    setData()
                } else {
                    val errorMessage = response.message()
                    Log.i("API", errorMessage)
                    Log.i("API", "GET FAILED")
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })
    }

    private fun init(view: View){
        backBtn = view.findViewById(R.id.backBtn)
        menuBtn = view.findViewById(R.id.menuBtn)

        backBtn.setOnClickListener {
            (this.activity as? MainActivity)?.goBack()
        }

        menuBtn.setOnClickListener {
            (this.activity as? MainActivity)?.openDrawer()
        }

        recyclerViewMovieList = view.findViewById(R.id.recyclerViewMovieList)
    }

    private fun setData(){
        recyclerViewMovieListAdapter = RecyclerViewMovieAdapter(this, movieList)
        recyclerViewMovieList.adapter = recyclerViewMovieListAdapter
        recyclerViewMovieList.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration: DividerItemDecoration = DividerItemDecoration(recyclerViewMovieList.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.item_divider)!!)
        recyclerViewMovieList.addItemDecoration(dividerItemDecoration)

        recyclerViewMovieListAdapter.onItemClick = {id ->
            val fragment = MovieDetailFragment()
            fragment.arguments = Bundle().apply {
                putInt("id", id)
            }
            (this.activity as? MainActivity)?.addFragment(fragment, "movie_detail")
        }
    }
}