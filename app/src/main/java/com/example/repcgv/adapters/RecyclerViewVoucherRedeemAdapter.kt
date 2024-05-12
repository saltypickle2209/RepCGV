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
import com.example.repcgv.models.Voucher

class RecyclerViewVoucherRedeemAdapter(private val fragment : Fragment, private val voucher: List<Voucher>): RecyclerView.Adapter<RecyclerViewVoucherRedeemAdapter.ViewHolder>() {
    lateinit var onItemClick: ((Int) -> Unit)

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewVoucherName = view.findViewById<TextView>(R.id.textViewVoucherName)
        val textViewVoucherPoint = view.findViewById<TextView>(R.id.textViewVoucherPoint)

        init{
            view.setOnClickListener {
                onItemClick.invoke(voucher[adapterPosition].id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.recyclerview_voucher_redeem_item_container, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return voucher.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Voucher = voucher[position]
        holder.textViewVoucherName.text = item.name
        holder.textViewVoucherPoint.text = item.point.toString()
    }
}