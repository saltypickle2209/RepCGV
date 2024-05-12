package com.example.repcgv.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.fragments.SeatSelectionFragment
import com.example.repcgv.models.Schedule

class ButtonTimeAdapter(
    private val fragment: Fragment,
    private val timeList: List<Schedule>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ButtonTimeAdapter.ButtonTimeViewHolder>() {

    inner class ButtonTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonTime: Button = itemView.findViewById(R.id.buttonTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonTimeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.time_button_layout, parent, false)
        return ButtonTimeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ButtonTimeViewHolder, position: Int) {
        val currentTime = timeList[position].scheduleStart
        holder.buttonTime.text = currentTime
        holder.buttonTime.setOnClickListener {
            onItemClick(currentTime)
            Log.d("chuyenSangKhac", "da chuyen" + timeList[position])
            val args = Bundle()
            args.putSerializable("schedule", timeList[position])
            Log.d("chuyenSangKhac", timeList[position].toString())

            val fragmentSeatSelection = SeatSelectionFragment()
            fragmentSeatSelection.arguments = args
            (fragment.activity as? MainActivity)?.addFragment(
                fragmentSeatSelection,
                "seatSelection"
            )
        }
    }

    override fun getItemCount(): Int {
        return timeList.size
    }
}