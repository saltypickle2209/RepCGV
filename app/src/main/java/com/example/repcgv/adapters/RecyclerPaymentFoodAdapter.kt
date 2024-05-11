package com.example.repcgv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repcgv.R
import com.example.repcgv.models.FoodPayment

class RecyclerPaymentFoodAdapter(
    private val fragment: Fragment,
    private val foods: List<FoodPayment>
) : RecyclerView.Adapter<RecyclerPaymentFoodAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var FoodImg: ImageView = view.findViewById(R.id.imageViewPoster)
        var FoodName: TextView = view.findViewById(R.id.textViewName)
        var FoodQuantity: TextView = view.findViewById(R.id.textViewQuantity)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerPaymentFoodAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view =
            inflater.inflate(R.layout.recycler_view_payment_food_item_container, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    override fun onBindViewHolder(holder: RecyclerPaymentFoodAdapter.ViewHolder, position: Int) {
        val currentCombo = foods[position]
        holder.FoodName.setText(currentCombo.name)
        holder.FoodQuantity.setText(currentCombo.quantity.toString())
        Glide.with(fragment).load(currentCombo.poster).into(holder.FoodImg)
    }
}