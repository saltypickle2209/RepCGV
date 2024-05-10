package com.example.repcgv.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repcgv.R
import com.example.repcgv.models.Movie

class ViewPagerMovieAdapter(private val fragment : Fragment, private val movieList : ArrayList<Movie>)
    : RecyclerView.Adapter<ViewPagerMovieAdapter.ViewHolder>(){

    lateinit var onItemClick: ((Int) -> Unit)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init{
            itemView.setOnClickListener {
                onItemClick.invoke(movieList[adapterPosition].id)
            }
        }
    }

    init{
        movieList.add(0, movieList.last())
        movieList.add(movieList[1])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item_container, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(fragment).load(movieList[position].poster).into(holder.imageView)
    }
}