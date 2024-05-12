package com.example.repcgv.adapters

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.R
import com.example.repcgv.models.CinemaSchedule


class RecyclerCinemaScheduleAdapter(private val fragment : Fragment, private val cinemaScheduleList: List<CinemaSchedule>) : RecyclerView.Adapter<RecyclerCinemaScheduleAdapter.CinemaScheduleViewHolder>() {
    var isExpanded = false
    class MarginItemDecoration(private val marginPx: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = marginPx // Đặt giá trị marginRight cho phần tử
        }
    }

    inner class CinemaScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCinemaName: TextView = itemView.findViewById(R.id.textViewCinemaName)
        val recyclerViewTime: RecyclerView = itemView.findViewById(R.id.recyclerViewTime)
        val conTainerLayout : ConstraintLayout = itemView.findViewById(R.id.Container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaScheduleViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.items_cinema_by_movie, parent, false)
//        return CinemaScheduleViewHolder(itemView)
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.items_cinema_by_movie, parent, false)
        Log.i("test","bbbb")
        return CinemaScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: CinemaScheduleViewHolder, position: Int) {
        val currentCinemaSchedule = cinemaScheduleList[position]
        holder.textViewCinemaName.text = currentCinemaSchedule.name
        holder.conTainerLayout.visibility = View.GONE

        holder.textViewCinemaName.setOnClickListener {
            // Thay đổi trạng thái của ConstraintLayout và xoay mũi tên
            if (isExpanded) {
                holder.conTainerLayout.visibility = View.GONE
                holder.textViewCinemaName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down, 0)
            } else {
                holder.conTainerLayout.visibility = View.VISIBLE
                holder.textViewCinemaName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.draw_up, 0)
            }
            // Cập nhật trạng thái
            isExpanded = !isExpanded
        }
        Log.i("CinemaScheduleAdapter", "Cinema Name: $currentCinemaSchedule")

        // Tạo Adapter cho RecyclerView hiển thị danh sách thời gian của rạp chiếu phim
//        val timeAdapter = ButtonTimeAdapter(currentCinemaSchedule.showTimes) { selectedTime ->
            // Xử lý sự kiện khi một thời gian được chọn

        val showTimes = currentCinemaSchedule.schedules // Mảng giả lập các thời gian
        val timeAdapter = ButtonTimeAdapter(fragment,showTimes) { selectedTime ->
            // Xử lý sự kiện khi một thời gian được chọn
            println("Selected time: $selectedTime")
        }

        holder.recyclerViewTime.adapter = timeAdapter
        holder.recyclerViewTime.adapter = timeAdapter

//        val marginInPixels = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.margin_size)
        holder.recyclerViewTime.addItemDecoration(MarginItemDecoration(20))
        holder.recyclerViewTime.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL,false)
    }

    override fun getItemCount(): Int {
        return cinemaScheduleList.size
    }
}