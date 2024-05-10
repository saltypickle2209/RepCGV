package com.example.repcgv.adapters


import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.R
import com.example.repcgv.models.Review

class RecyclerViewReviewListAdapter(private val fragment : Fragment, private val reviews: List<Review>): RecyclerView.Adapter<RecyclerViewReviewListAdapter.ViewHolder>()
{
    private val COLOR_STAR_SELECTED = Color.parseColor("#FFEB3B")
    private val COLOR_STAR_NONE = Color.parseColor("#000000")
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textReviewerName = view.findViewById<TextView>(R.id.textReviewerName);
        val textReview = view.findViewById<TextView>(R.id.textReview);

        val buttonStarsList = arrayListOf<AppCompatButton>(
            view.findViewById(R.id.buttonStar_0),
            view.findViewById(R.id.buttonStar_1),
            view.findViewById(R.id.buttonStar_2),
            view.findViewById(R.id.buttonStar_3),
            view.findViewById(R.id.buttonStar_4),
        )

        init{
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewReviewListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.recyclerview_review_item_container, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Review = reviews[position]
        holder.textReview.text = item.review
        holder.textReviewerName.text = item.userName

        for(i in 0 until 5){
            holder.buttonStarsList[i].backgroundTintList = ColorStateList.valueOf( COLOR_STAR_NONE);
        }

        for(i in 0 until item.reviewStar){
            holder.buttonStarsList[i].backgroundTintList = ColorStateList.valueOf(COLOR_STAR_SELECTED);
        }
    }
}