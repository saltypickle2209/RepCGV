package com.example.repcgv.fragments

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.DateAdapter
import com.example.repcgv.adapters.RecyclerCinemaScheduleAdapter
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.api.ScheduleApi
import com.example.repcgv.models.CinemaSchedule
import com.example.repcgv.models.OnDateSelectedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BookingTimeFragment : Fragment(), OnDateSelectedListener {
    class MarginItem(private val marginPx: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = marginPx // Đặt giá trị marginRight cho phần tử
        }
    }
    // TODO: Rename and change types of parameters
    var selectedRadioButton : RadioButton?=null
    var textViewToday : TextView?= null
    var textViewNextDay1 : TextView?= null
    var textViewNextDay2 : TextView?= null
    var textViewNextDay3 : TextView?= null
    var textViewNextDay4 : TextView?= null
    var textViewNextDay5 : TextView?= null
    var textViewNextDay6 : TextView?= null
    var textDateSelected : TextView?= null
    private lateinit var recyclerViewTime : RecyclerView
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton
    private var movieID = 932420
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booking_time, container, false)
        textViewToday = view.findViewById(R.id.textViewToday)
        textViewNextDay1 = view.findViewById(R.id.textViewNextDay1)
        textViewNextDay2 = view.findViewById(R.id.textViewNextDay2)
        textViewNextDay3 = view.findViewById(R.id.textViewNextDay3)
        textViewNextDay4 = view.findViewById(R.id.textViewNextDay4)
        textViewNextDay5 = view.findViewById(R.id.textViewNextDay5)
        textViewNextDay6 = view.findViewById(R.id.textViewNextDay6)
        textDateSelected = view.findViewById(R.id.textView10)
        init(view)
        initDate(view)
        recyclerViewTime = view.findViewById(R.id.recycleViewCinemaSchedules)
        val cinemaList = listOf("CGV - Tân Phú", "Galaxy - Hoàng Văn Thụ", "BHD - Vincom", "Lotte - Cantavil")


        recyclerViewTime.addItemDecoration(MarginItem(20))

        recyclerViewTime.layoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.VERTICAL,false)
        return view
    }
    private  fun  init(view: View)
    {
        backBtn = view.findViewById(R.id.backBtn)
        menuBtn = view.findViewById(R.id.menuBtn)

        backBtn.setOnClickListener {
            (this.activity as? MainActivity)?.goBack()
        }

        menuBtn.setOnClickListener {
            (this.activity as? MainActivity)?.openDrawer()
        }
        val viewPager2: ViewPager2 = view.findViewById(R.id.DateFragment)
        val adapter = DateAdapter(childFragmentManager, lifecycle)
        val fragment = DateSelectedFragment()
        val args = Bundle()
        val calendar = Calendar.getInstance()
        val selectedDate = calendar.get(Calendar.DAY_OF_MONTH)
        args.putInt("start_date", selectedDate)
        fragment.arguments = args
        fragment.setOnDateSelectedListener(this)
        val fragment2 = DateSelectedFragment()
        val ags2 = Bundle()
        ags2.putInt("start_date", selectedDate+7)
        fragment2.arguments = ags2
        fragment2.setOnDateSelectedListener(this)
        adapter.addFragment(fragment)
        adapter.addFragment(fragment2)

        viewPager2.adapter = adapter
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Xử lý khi trang được chọn
                if (position == 0) {
                    // Trang đầu tiên
                    // Lấy và xử lý sự kiện từ fragment 1
                    textViewToday?.text = "Hôm nay"
                    textViewToday?.setTextColor(Color.argb(255, 125, 0, 1))
                } else if (position == 1) {
                    initDate(view)
                    textViewToday?.setTextColor(Color.parseColor("#666666"))
                    // Trang thứ hai
                    // Lấy và xử lý sự kiện từ fragment 2
                }
            }
        })
    }
    override fun onDateSelected(date: Date, radioButton: RadioButton) {
        // Cập nhật RecyclerView dựa trên ngày được chọn
        val dateFormat = SimpleDateFormat("EEEE dd 'tháng' MM, yyyy", Locale.getDefault())
        val dateSelectedFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateSelected = dateSelectedFormat.format(date)
        val formattedDate = dateFormat.format(date)
        if(selectedRadioButton!=null)
        {
            selectedRadioButton?.isChecked =false
        }
        selectedRadioButton = radioButton
        textDateSelected?.setText(formattedDate)
        val scheduleService = RetrofitClient.instance.create(ScheduleApi::class.java)
        arguments?.takeIf { it.containsKey("movieId") }?.apply {
            movieID = getInt("movieId")
        }
        val call = scheduleService.getSchedulesByDateMovieCinema(dateSelected, movieID)
        call.enqueue(object : Callback<List<CinemaSchedule>> {
            override fun onResponse(call: Call<List<CinemaSchedule>>, response: Response<List<CinemaSchedule>>) {
                if (response.isSuccessful) {
                    val schedules = response.body()
                    val  cinemaAdapter = RecyclerCinemaScheduleAdapter(this@BookingTimeFragment,schedules!!)
                    recyclerViewTime.adapter = cinemaAdapter
                    Log.d("BookingTimeFragment", "Successful response: $schedules")
                    // Xử lý dữ liệu nhận được
                } else {
                    // Xử lý lỗi
                    Log.e("BookingTimeFragment", "Error: " + response.code() + " - " + response.message())

                }
            }

            override fun onFailure(call: Call<List<CinemaSchedule>>, t: Throwable) {
                // Xử lý lỗi
                Log.e("BookingTimeFragment", "Error: " + t.message)

            }
        })

        Log.d("Checked RadioButton", "Selected RadioButton: $formattedDate")

    }
    fun initDate(view: View) {

        val currentDate = Calendar.getInstance().time // Lấy ngày hiện tại
        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        for (i in 0..6) {
            val textView = getTextViewByIndex(i)
            calendar.add(Calendar.DAY_OF_MONTH, i) // Tăng ngày trong tháng

            // Lấy thứ của ngày hiện tại
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            // Đặt nội dung cho các TextView dựa vào thứ của ngày hiện tại
            when (dayOfWeek) {
                Calendar.SUNDAY -> textView?.text = "CN"
                Calendar.MONDAY -> textView?.text = "T2"
                Calendar.TUESDAY -> textView?.text = "T3"
                Calendar.WEDNESDAY -> textView?.text = "T4"
                Calendar.THURSDAY -> textView?.text = "T5"
                Calendar.FRIDAY -> textView?.text = "T6"
                Calendar.SATURDAY -> textView?.text = "T7"
                else -> textView?.text = "" // Trường hợp không xác định
            }

            calendar.time = currentDate // Đặt lại ngày hiện tại cho ngày ban đầu
        }
    }

    fun getTextViewByIndex(index: Int): TextView? {
        return when (index) {
            0 -> textViewToday
            1 -> textViewNextDay1
            2 -> textViewNextDay2
            3 -> textViewNextDay3
            4 -> textViewNextDay4
            5 -> textViewNextDay5
            6 -> textViewNextDay6
            else -> throw IllegalArgumentException("Invalid index")
        }
    }

}