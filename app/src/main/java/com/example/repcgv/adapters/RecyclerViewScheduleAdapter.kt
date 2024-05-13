package com.example.repcgv.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repcgv.R
import com.example.repcgv.api.CinemaApi
import com.example.repcgv.api.MovieApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.api.ScheduleApi
import com.example.repcgv.models.Cinema
import com.example.repcgv.models.Movie
import com.example.repcgv.models.Schedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class RecyclerViewScheduleAdapter(private val fragment : Fragment, private var schedules: List<Schedule>) : RecyclerView.Adapter<RecyclerViewScheduleAdapter.ViewHolder>() {
    private val movieService = RetrofitClient.instance.create(MovieApi::class.java)
    private val cinemaApi = RetrofitClient.instance.create(CinemaApi::class.java)
    private val scheduleApi = RetrofitClient.instance.create(ScheduleApi::class.java)
    lateinit var onItemClick: ((Int) -> Unit)

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val moviePoster = view.findViewById<ImageView>(R.id.moviePoster)
        val textMovieName = view.findViewById<TextView>(R.id.textMovieName)
        val textCinemaName = view.findViewById<TextView>(R.id.textCinema)
        val textDate = view.findViewById<TextView>(R.id.textDate)
        val textTime = view.findViewById<TextView>(R.id.textTime)
        val textRoom = view.findViewById<TextView>(R.id.textRoom)
        val textTicketSold = view.findViewById<TextView>(R.id.textTicketSold)

        init{
            view.setOnClickListener {
                onItemClick.invoke(schedules[adapterPosition].scheduleId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.recyclerview_schedule_item_container, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Schedule = schedules[position]
        val date = Date(item.scheduleDate * 1000)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = sdf.format(date)
        holder.textDate.text = formattedDate
        holder.textTime.text = item.scheduleStart
        holder.textRoom.text = "Room ${item.roomId}"



        movieService.getMovieByID(item.movieId).enqueue(fragment, object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    holder.textMovieName.text = response.body()!!.name
                    Glide.with(fragment).load(response.body()!!.poster).into(holder.moviePoster)
                } else {
                    holder.textMovieName.text = item.movieId.toString()
                }
            }
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })


        cinemaApi.getCinemaById(item.cinemaId).enqueue(fragment, object : Callback<Cinema> {
            override fun onResponse(call: Call<Cinema>, response: Response<Cinema>) {
                if (response.isSuccessful) {
                    holder.textCinemaName.text = response.body()!!.name
                } else {
                    holder.textCinemaName.text = item.cinemaId.toString()
                }
            }
            override fun onFailure(call: Call<Cinema>, t: Throwable) {
                Log.i("API", "Get cinema failed")
                Log.i("API", t.message!!)
                holder.textCinemaName.text = item.cinemaId.toString()
            }
        })


        scheduleApi.getScheduleTickets(item.scheduleId).enqueue(fragment, object: Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val res =response.body()!!
                    holder.textTicketSold.text = "${res.count()} tickets sold"

                } else {
                    holder.textTicketSold.text = "-1 ticket sold"
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.i("API", "Get cinema failed")
                Log.i("API", t.message!!)
                holder.textCinemaName.text = item.cinemaId.toString()
            }
        })
    }

    fun <T> Call<T>.enqueue(lifecycleOwner: LifecycleOwner, callback: Callback<T>) {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun cancelCalls() {
                this@enqueue.cancel()
            }
        })
        this.enqueue(callback)
    }


    fun updateList(newList: List<Schedule>){
        schedules = newList
        notifyDataSetChanged()
    }
}