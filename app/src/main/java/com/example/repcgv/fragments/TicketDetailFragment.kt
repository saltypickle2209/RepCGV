package com.example.repcgv.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.api.OrderApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.models.Ticket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class TicketDetailFragment : Fragment() {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var imageViewAdvertisement: ImageView

    private lateinit var imageViewMovieImage: ImageView
    private lateinit var textViewMovieName: TextView
    private lateinit var movieClassification: Button
    private lateinit var textViewBookingDate: TextView
    private lateinit var textViewBookingTime: TextView
    private lateinit var textViewCinemaName: TextView
    private lateinit var textViewCinemaAddress: TextView
    private lateinit var textViewCinemaRoom: TextView
    private lateinit var textViewSeat: TextView
    private lateinit var textViewTicketPrice: TextView
    private lateinit var textViewFoodBeveragePrice: TextView
    private lateinit var textViewDiscount: TextView
    private lateinit var textViewPaymentMethod: TextView
    private lateinit var textViewTotal: TextView

    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        arguments?.takeIf { it.containsKey("id") }?.apply {
            id = getInt("id")
            fetchData()
        }
    }

    private fun init(view: View){
        backBtn = view.findViewById(R.id.backBtn)
        menuBtn = view.findViewById(R.id.menuBtn)

        backBtn.setOnClickListener {
            (this.activity as? MainActivity)?.goBack()
        }

        menuBtn.setOnClickListener {
            (this.activity as? MainActivity)?.openDrawer()
        }

        imageViewAdvertisement = view.findViewById(R.id.imageViewAdvertisement)

        imageViewMovieImage = view.findViewById(R.id.imageViewMovieImage)
        textViewMovieName = view.findViewById(R.id.textViewMovieName)
        movieClassification = view.findViewById(R.id.movieClassification)
        textViewBookingDate = view.findViewById(R.id.textViewBookingDate)
        textViewBookingTime = view.findViewById(R.id.textViewBookingTime)
        textViewCinemaName = view.findViewById(R.id.textViewCinemaName)
        textViewCinemaAddress = view.findViewById(R.id.textViewCinemaAddress)
        textViewCinemaRoom = view.findViewById(R.id.textViewCinemaRoom)
        textViewSeat = view.findViewById(R.id.textViewSeat)
        textViewTicketPrice = view.findViewById(R.id.textViewTicketPrice)
        textViewFoodBeveragePrice = view.findViewById(R.id.textViewFoodBeveragePrice)
        textViewDiscount = view.findViewById(R.id.textViewDiscount)
        textViewPaymentMethod = view.findViewById(R.id.textViewPaymentMethod)
        textViewTotal = view.findViewById(R.id.textViewTotal)

        Glide.with(this)
            .load("https://iguov8nhvyobj.vcdn.cloud/media/banner/cache/1/b58515f018eb873dafa430b6f9ae0c1e/9/8/980x448_4_-min_2.jpg")
            .into(imageViewAdvertisement)
    }

    private fun fetchData(){
        val orderService = RetrofitClient.instance.create(OrderApi::class.java)
        val call = orderService.getDetailedOrderById(id)

        call.enqueue(object : Callback<Ticket> {
            override fun onResponse(call: Call<Ticket>, response: Response<Ticket>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val ticket = response.body()!!

                    val date = Date(ticket.scheduleDate * 1000)
                    val ticketDate = SimpleDateFormat("yyyy-MM-dd").format(date)

                    Glide.with(this@TicketDetailFragment).load(ticket.moviePoster).into(imageViewMovieImage)
                    textViewMovieName.text = ticket.movieName
                    textViewBookingDate.text = ticketDate
                    textViewBookingTime.text = ticket.scheduleStart
                    textViewCinemaName.text = ticket.cinemaName
                    textViewCinemaAddress.text = ticket.cinemaAddress

                    val cinemaRoom = "Room ${ticket.roomId}"
                    textViewCinemaRoom.text = cinemaRoom
                    textViewSeat.text = ticket.ticket.joinToString(", ")

                    val totalTicketPrice = "${ticket.totalTicket} đ"
                    textViewTicketPrice.text = totalTicketPrice

                    val totalFoodPrice = "${ticket.totalFood} đ"
                    textViewFoodBeveragePrice.text = totalFoodPrice

                    val discount = "${ticket.discount} đ"
                    textViewDiscount.text = discount
                    textViewPaymentMethod.text = ticket.paymentMethod

                    val total = "${ticket.total} đ"
                    textViewTotal.text = total

                    setMovieClassification(ticket)
                } else {
                    Toast.makeText(requireContext(), "Can't get ticket's data!", Toast.LENGTH_SHORT).show()
                    (this@TicketDetailFragment.activity as? MainActivity)?.goBack()
                }
            }

            override fun onFailure(call: Call<Ticket>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })
    }

    private fun setMovieClassification(item: Ticket){
        if(item.movieClassification == null)
            return
        when(item.movieClassification){
            "P" -> {
                movieClassification.text = "P"
                movieClassification.setTextColor(Color.parseColor("#799D46"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#799D46"))
            }
            "K" -> {
                movieClassification.text = "K"
                movieClassification.setTextColor(Color.parseColor("#1A9ABD"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#1A9ABD"))
            }
            "T13" -> {
                movieClassification.text = "T13"
                movieClassification.setTextColor(Color.parseColor("#E8E10A"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8E10A"))
            }
            "T16" -> {
                movieClassification.text = "T16"
                movieClassification.setTextColor(Color.parseColor("#F3A001"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F3A001"))
            }
            "T18" -> {
                movieClassification.text = "T18"
                movieClassification.setTextColor(Color.parseColor("#EA3B24"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EA3B24"))
            }
            else -> {
                movieClassification.text = "U"
                movieClassification.setTextColor(Color.parseColor("#000000"))
                movieClassification.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            }
        }
    }
}