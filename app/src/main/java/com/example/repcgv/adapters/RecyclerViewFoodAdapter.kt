package com.example.repcgv.adapters

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
import com.example.repcgv.models.Food
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class RecyclerViewFoodAdapter(private val fragment: Fragment, private val foods: List<Food>) :
    RecyclerView.Adapter<RecyclerViewFoodAdapter.ViewHolder>() {
    lateinit var onFoodAdd: ((Int) -> Unit)
    lateinit var onFoodRemove: ((Int) -> Unit)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagePoster = view.findViewById<ImageView>(R.id.imagePoster);
        val textFoodName = view.findViewById<TextView>(R.id.textFoodName);
        val textFoodDescription = view.findViewById<TextView>(R.id.textFoodDescription);
        val buttonAdd = view.findViewById<Button>(R.id.btnAdd);
        val buttonMinus = view.findViewById<Button>(R.id.btnMinus);
        val textAmount = view.findViewById<TextView>(R.id.textAmount);

        init {
            buttonAdd.setOnClickListener {
                val currentAmount = textAmount.text.toString().toIntOrNull() ?: 0
                val newAmount = currentAmount + 1
                textAmount.text = newAmount.toString()
                onFoodAdd.invoke(foods[adapterPosition].id)
            }

            buttonMinus.setOnClickListener {
                val currentAmount = textAmount.text.toString().toIntOrNull() ?: 0
                if (currentAmount > 0) {
                    val newAmount = currentAmount - 1
                    textAmount.text = newAmount.toString()
                    onFoodRemove.invoke(foods[adapterPosition].id)
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewFoodAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.recyclerview_food_item_container, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Food = foods[position]

        holder.textFoodName.text = item.name + " - " + formatToVND(item.price);
        holder.textFoodDescription.text = item.description;
        Glide.with(fragment).load(item.poster).into(holder.imagePoster)

        Log.i("View", "Checked");

    }

    fun formatToVND(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        formatter.currency = Currency.getInstance("VND")
        return formatter.format(amount)
    }
}