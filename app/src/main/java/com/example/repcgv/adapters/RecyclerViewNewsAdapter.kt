package com.example.repcgv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repcgv.models.News
import com.example.repcgv.R

class RecyclerViewNewsAdapter(private val fragment : Fragment, private val news: List<News>): RecyclerView.Adapter<RecyclerViewNewsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cardImage = view.findViewById<ImageView>(R.id.cardImage)
        val cardText = view.findViewById<TextView>(R.id.cardText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.recyclerview_news_item_container, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: News = news[position]
        holder.cardText.text = item.title
        Glide.with(fragment).load(item.image).into(holder.cardImage)
    }
}