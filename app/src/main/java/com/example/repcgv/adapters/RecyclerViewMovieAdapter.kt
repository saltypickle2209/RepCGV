package com.example.repcgv.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repcgv.R
import com.example.repcgv.api.PersonApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.models.Movie
import com.example.repcgv.models.Person
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewMovieAdapter(private val fragment : Fragment, private var movies: List<Movie>): RecyclerView.Adapter<RecyclerViewMovieAdapter.ViewHolder>() {
    lateinit var onItemClick: ((Int) -> Unit)
    private val personService = RetrofitClient.instance.create(PersonApi::class.java)

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val moviePoster = view.findViewById<ImageView>(R.id.moviePoster)
        val textViewMovieName = view.findViewById<TextView>(R.id.textViewMovieName)
        val textViewMovieDirector = view.findViewById<TextView>(R.id.textViewMovieDirector)
        val textViewMoviePremiereDate = view.findViewById<TextView>(R.id.textViewMoviePremiereDate)
        val textViewMovieDuration = view.findViewById<TextView>(R.id.textViewMovieDuration)
        val movieClassification = view.findViewById<Button>(R.id.movieClassification)

        init{
            view.setOnClickListener {
                onItemClick.invoke(movies[adapterPosition].id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.recyclerview_movie_item_container, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Movie = movies[position]
        holder.textViewMovieName.text = item.name
        holder.textViewMoviePremiereDate.text = item.premiereDate.split("T")[0]
        val durationString = item.duration.toString() + " min"
        holder.textViewMovieDuration.text = durationString
        Glide.with(fragment).load(item.poster).into(holder.moviePoster)

        if(item.classification != null){
            when(item.classification){
                "P" -> {
                    holder.movieClassification.text = "P"
                    holder.movieClassification.setTextColor(Color.parseColor("#799D46"))
                    holder.movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#799D46"))
                }
                "K" -> {
                    holder.movieClassification.text = "K"
                    holder.movieClassification.setTextColor(Color.parseColor("#1A9ABD"))
                    holder.movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#1A9ABD"))
                }
                "T13" -> {
                    holder.movieClassification.text = "T13"
                    holder.movieClassification.setTextColor(Color.parseColor("#E8E10A"))
                    holder.movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8E10A"))
                }
                "T16" -> {
                    holder.movieClassification.text = "T16"
                    holder.movieClassification.setTextColor(Color.parseColor("#F3A001"))
                    holder.movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F3A001"))
                }
                "T18" -> {
                    holder.movieClassification.text = "T18"
                    holder.movieClassification.setTextColor(Color.parseColor("#EA3B24"))
                    holder.movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EA3B24"))
                }
                else -> {
                    holder.movieClassification.text = "U"
                    holder.movieClassification.setTextColor(Color.parseColor("#000000"))
                    holder.movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                }
            }
        }


        val call = personService.getPersonByID(item.director)

        call.enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.isSuccessful) {
                    holder.textViewMovieDirector.text = response.body()!!.name
                } else {
                    holder.textViewMovieDirector.text = item.director.toString()
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })
    }

    fun updateList(newList: List<Movie>){
        movies = newList
        notifyDataSetChanged()
    }

}