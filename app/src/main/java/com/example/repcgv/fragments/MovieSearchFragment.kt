package com.example.repcgv.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.RecyclerViewMovieAdapter
import com.example.repcgv.api.MovieApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieSearchFragment : Fragment() {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var editTextSearchQuery: EditText
    private lateinit var toggleButtonFilter: ToggleButton

    private lateinit var recyclerViewMovieList: RecyclerView
    private lateinit var recyclerViewMovieListAdapter: RecyclerViewMovieAdapter

    private var movieList: ArrayList<Movie> = ArrayList()
    private val movieService = RetrofitClient.instance.create(MovieApi::class.java)
    private var currentlyShowing = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
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

        editTextSearchQuery = view.findViewById(R.id.editTextSearchQuery)
        toggleButtonFilter = view.findViewById(R.id.toggleButtonFilter)

        recyclerViewMovieList = view.findViewById(R.id.recyclerViewMovieList)

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

        // Search operation
        editTextSearchQuery.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH && !editTextSearchQuery.text.isEmpty()){
                fetchData()
            }
            true
        }

        toggleButtonFilter.setOnCheckedChangeListener { buttonView, isChecked ->
            currentlyShowing = isChecked
            fetchData()
        }
    }

    private fun fetchData(){

    }

}