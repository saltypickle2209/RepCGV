package com.example.repcgv.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.api.MovieApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.api.RoomApi
import com.example.repcgv.api.ScheduleApi
import com.example.repcgv.models.Movie
import com.example.repcgv.models.Room
import com.example.repcgv.models.Schedule
import com.example.repcgv.models.Seat
import com.example.repcgv.views.SeatButton
import com.example.repcgv.views.ZoomLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


class SeatSelectionFragment : Fragment(), View.OnTouchListener {
    private lateinit var mainLayout: RelativeLayout //The seats is put here to navigate
    private lateinit var outerLayout: RelativeLayout
    private lateinit var root: View
    private lateinit var zoomLayout: ZoomLayout

    private lateinit var textMovieName: TextView;
    private lateinit var textScreenType: TextView;
    private lateinit var textPrice: TextView;
    private lateinit var textSeatSelected: TextView;

    private lateinit var buttonPay: Button;

    private var mainID: Int = 0;

    private val SEAT_SIZE = 125;
    private val SEAT_PADDING = 5;

    private val PADDING_HOR = 300;
    private val PADDING_VER = 500;

    private lateinit var seats: Array<Array<Seat>>
    private lateinit var bookedSeats: List<String>
    val chosenSeats: MutableCollection<Seat> = mutableListOf();

    var scheduleID = 955958

    lateinit var schedule: Schedule;
    lateinit var room: Room;
    lateinit var movie: Movie;

    var userId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_seat_selection, container, false)
        arguments?.takeIf {
            it.containsKey("schedule")
        }?.apply {
            schedule = getSerializable("schedule") as Schedule
        }
        arguments?.takeIf {
            it.containsKey("userId")
        }?.apply {
            userId = getString("userId")!!
        }
        Log.i("API", schedule.scheduleId.toString())
        Log.i("API", "User id: " + userId)

        init();
        //TODO: add a step to fetch data here
        fetchMovieData(schedule.movieId);
        return root;
    }

    private fun init() {
        zoomLayout = root.findViewById(R.id.zoomLayout);
        outerLayout = root.findViewById(R.id.outerLayout);

        textMovieName = root.findViewById(R.id.textMovieName)
        textScreenType = root.findViewById(R.id.textScreenType)
        textSeatSelected = root.findViewById(R.id.textSeatSelected)
        textPrice = root.findViewById(R.id.textPrice)

        buttonPay = root.findViewById(R.id.buttonPay)

        buttonPay.setOnClickListener {
            if (chosenSeats.count() == 0) return@setOnClickListener;
            var fragment = FoodOrderFragment()
            val args = Bundle()
            val idsString = chosenSeats.joinToString(separator = "|") { it.name.toString() }
            Log.i("API", "Test: " + idsString)
            args.putSerializable("schedule", schedule)
            args.putString("selectedSeatID", idsString)
            args.putString("userId", userId)
            args.putDouble("currentPrice", chosenSeats.sumOf { it.price })
            args.putString("movieName", movie.name)
            fragment.arguments = args;
            (this.activity as? MainActivity)?.addFragment(fragment, "food")
        }
    }

    private fun fetchScheduleData() {
        Log.i("API", scheduleID.toString())

        val scheduleService = RetrofitClient.instance.create(ScheduleApi::class.java)
        val call = scheduleService.GetScheduleByID(scheduleID)
        call.enqueue(object : Callback<Schedule> {
            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                if (response.isSuccessful) {
                    schedule = response.body()!!
                    Log.i("API", schedule.scheduleId.toString())
                    fetchMovieData(schedule.movieId);
                } else {
                    val errorMessage = response.message()
                    Log.i("API", errorMessage)
                    Log.i("API", "GET FAILED")
                }
            }

            override fun onFailure(call: Call<Schedule>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })
    }

    private fun fetchRoomData(roomID: Int) {
        val roomSerivce = RetrofitClient.instance.create(RoomApi::class.java)
        val call = roomSerivce.GetRoomById(roomID)
        call.enqueue(object : Callback<Room> {
            override fun onResponse(call: Call<Room>, response: Response<Room>) {
                if (response.isSuccessful) {
                    room = response.body()!!
                    Log.i("API", "Fetched Room")
                    fetchBookedSeats()

                } else {
                    val errorMessage = response.message()
                    Log.i("API", errorMessage)
                    Log.i("API", "GET FAILED")
                }
            }

            override fun onFailure(call: Call<Room>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })
    }

    private fun fetchMovieData(movieID: Int) {
        val movieService = RetrofitClient.instance.create(MovieApi::class.java)
//         TODO: Uncomment this when the API is ready
//        val call = movieService.getMovieByID(movieID)
//        call.enqueue(object : Callback<Movie> {
//            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
//                if (response.isSuccessful) {
//                    movie = response.body()!!
//                    Log.i("API", "Fetched Movie")
//
//                    fetchRoomData(schedule.roomId)
//                } else {
//                    val errorMessage = response.message()
//                    Log.i("API", errorMessage)
//                    Log.i("API", "GET FAILED")
//                }
//            }
//
//            override fun onFailure(call: Call<Movie>, t: Throwable) {
//                Log.i("API", t.message!!)
//            }
//        })
    }

    private fun fetchBookedSeats() {
        val scheduleService = RetrofitClient.instance.create(ScheduleApi::class.java)
        val call = scheduleService.getScheduleTickets(schedule.scheduleId)
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    bookedSeats = response.body()!!
                    updateOrderDetails()
                    setupView()
                    populateView()
                    Log.i(
                        "API",
                        "Fetched Booked Seats: " + bookedSeats.count().toString() + " tickets"
                    )
                } else {
                    val errorMessage = response.message()
                    Log.i("API", errorMessage)
                    Log.i("API", "GET FAILED")
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })
    }


    private fun updateOrderDetails() {
        textMovieName.text = movie.name
        textPrice.text = formatToVND(chosenSeats.sumOf { it.price });
        textSeatSelected.text = "${chosenSeats.count()} Seats Selected";
    }


    private fun setupView() {
        val columns = room.columns;
        val rows = room.rows

        var width = PADDING_VER * 2 + (SEAT_SIZE + SEAT_PADDING) * columns
        var height = PADDING_HOR * 2 + (SEAT_SIZE + SEAT_PADDING) * (1 + rows)

        Log.i("ZoomLayout", width.toString())

        zoomLayout.SetSize(width, height)
        mainLayout = RelativeLayout(zoomLayout.context);
        val displayMetrics = resources.displayMetrics
        val layoutParams = RelativeLayout.LayoutParams(width, height)

        var extraWidth = width - displayMetrics.widthPixels;
        var extraHeight = height - displayMetrics.heightPixels;

        layoutParams.setMargins(-(extraWidth / 2), 0, -(extraWidth / 2), -(extraHeight))

        mainID = View.generateViewId()
        mainLayout.id = mainID
        mainLayout.layoutParams = layoutParams;

        zoomLayout.setOnTouchListener(this)
        zoomLayout.addView(mainLayout)

        //Screen Text
        var txtScreen = TextView(mainLayout.context)
        val txtScreenLayoutParams = RelativeLayout.LayoutParams(500, 100)
        txtScreenLayoutParams.setMargins(width / 2 - 250, 100, -(width / 2 - 250), 0)
        txtScreen.layoutParams = txtScreenLayoutParams
        txtScreen.text = "SCREEN"
        txtScreen.textSize = 26.0f
        txtScreen.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL

        mainLayout.addView(txtScreen)
    }

    fun populateView() {
        val PADDING_TOP = PADDING_HOR
        val columns = room.columns;
        val rows = room.rows

        //TODO: Get this from the DATABASE
        seats = Array(rows) { row ->
            Array(columns) { column ->
                val id = row * columns + column
                val seatStatus =
                    Seat.Companion.SeatStatus.fromInt(room.seats[id])

                Seat(
                    "A0",
                    id,
                    seatStatus,
                    seatStatus,
                    seatStatus.color,
                    seatStatus.price
                )
            }
        }

        for (y in 0 until rows) {
            for (x in 0 until columns) {
                val seatName = mapToAlphabet(y) + x.toString();
                seats[y][x].name = seatName;
                if (seats[y][x].name in bookedSeats) {
                    Log.i("API", "Booked: " + seats[y][x].name)
                    seats[y][x].defaultStatus = Seat.Companion.SeatStatus.Booked
                    seats[y][x].status = Seat.Companion.SeatStatus.Booked
                    seats[y][x].defaultColor = Seat.Companion.SeatStatus.Booked.color
                }

                //Log.i("SEAT", x.toString() + "|" + y.toString() +": " + seats[y][x].status.toString())
            }
        }


        for (y in 0 until rows) {
            for (x in 0 until columns) {

                var seatButton = SeatButton(mainLayout.context)
                seatButton.elevation = 0f
                seatButton.stateListAnimator = null

                val layoutParams = RelativeLayout.LayoutParams(SEAT_SIZE, SEAT_SIZE)
                layoutParams.setMargins(
                    x * (SEAT_SIZE + SEAT_PADDING) + PADDING_VER,
                    y * (SEAT_SIZE + SEAT_PADDING) + PADDING_TOP,
                    0,
                    0
                )
                seatButton.setLayoutParams(layoutParams)

                var seatData = seats[y][x]

                seatButton.setTextColor(Color.parseColor("#FFFFFF"))
                seatButton.setBackgroundColor(seatData.defaultColor)
                if (seatData.status != Seat.Companion.SeatStatus.None) seatButton.text =
                    seatData.name



                seatButton.setOnClickListener {
                    when (seatData.status) {
                        Seat.Companion.SeatStatus.None -> {

                        }

                        Seat.Companion.SeatStatus.Booked -> {

                        }

                        Seat.Companion.SeatStatus.Choosing -> {
                            seatButton.setBackgroundColor(seatData.defaultColor)
                            seatData.status = seatData.defaultStatus
                            chosenSeats.remove(seatData);

                        }

                        else -> {
                            seatButton.setBackgroundColor(Seat.COLOR_CHOOSING)
                            seatData.status = Seat.Companion.SeatStatus.Choosing
                            chosenSeats.add(seatData);
                        }

                    }
                    updateOrderDetails()
                }
                mainLayout.addView(seatButton)
            }
        }
    }

    fun mapToAlphabet(n: Int): Char {
        require(n >= 0) { "Input value must be non-negative" }
        return (n + 65).toChar()
    }

    fun CreateSeatArray(width: Int, height: Int): Array<Array<Int>> {
        return Array(height) { Array(width) { 0 } }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        zoomLayout.init(this.context);
        return false;
    }


    fun formatToVND(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        formatter.currency = Currency.getInstance("VND")
        return formatter.format(amount)
    }
}