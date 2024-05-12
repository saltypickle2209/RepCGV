package com.example.repcgv.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.RecyclerPaymentFoodAdapter
import com.example.repcgv.api.CinemaApi
import com.example.repcgv.api.FoodApi
import com.example.repcgv.api.MovieApi
import com.example.repcgv.api.OrderApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.models.Cinema
import com.example.repcgv.models.FoodPayment
import com.example.repcgv.models.Movie
import com.example.repcgv.models.Order
import com.example.repcgv.zalo.CreateOrder
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentPreviewFragment : Fragment() {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var voucherBtn: Button
    private lateinit var couponBtn: Button
    private lateinit var paymentButton:Button

    private  lateinit var textViewMovieName: TextView
    private  lateinit var textViewBookingDate: TextView
    private  lateinit var textViewBookingTime: TextView
    private  lateinit var textViewCinemaName: TextView
    private  lateinit var textViewCinemaRoom: TextView
    private  lateinit var textViewSeat: TextView
    private  lateinit var textViewFoodAndDrink: TextView
    private  lateinit var textViewOriginalTotal: TextView
    private  lateinit var imageViewMoviePoster: ImageView
    private  lateinit var textViewTicketQuantity:TextView
    private  lateinit var textViewTicketTotal:TextView
    private  lateinit var textViewFoodAndBeverageTotal:TextView
    private  lateinit var textViewSubtotal :TextView
    private  lateinit var textViewTotal :TextView

    private  lateinit var recyclerViewFoodAndBeverageList:RecyclerView
    private  lateinit var FoodPaymentAdapter :RecyclerPaymentFoodAdapter

    private  lateinit var ListFood : List<FoodPayment>

    private lateinit var  order: Order
    private  var movie: Movie ?= null
    private  var cinema: Cinema?= null
    private val movieService = RetrofitClient.instance.create(MovieApi::class.java)
    private val cinemaService = RetrofitClient.instance.create(CinemaApi::class.java)
    private val orderService = RetrofitClient.instance.create(OrderApi::class.java)
    private val foodService = RetrofitClient.instance.create(FoodApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment_preview, container, false)

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        init(view)
        fetchData()
        return view
    }


    private fun setData()
    {
        val date = Date(order.time)

// Định dạng ngày
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(date)

// Định dạng giờ
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeString = timeFormat.format(date)
        textViewMovieName.setText(movie?.name)
        Log.i("movieName",movie!!.name)
        textViewBookingDate.setText(dateString)
        textViewBookingTime.setText(timeString)
        textViewCinemaName.setText(movie?.name)
        textViewOriginalTotal.setText(order.total.toString())
        textViewTicketQuantity.setText(order.tickets.count().toString())
        textViewTicketTotal.setText(order.totalTicket.toString())
        textViewFoodAndBeverageTotal.setText(order.totalFood.toString())
        textViewSeat.setText(order.tickets.joinToString(separator = ", "))
        textViewSubtotal.setText(order.total.toString())
        textViewTotal.setText(order.total.toString())
        Glide.with(this).load(movie?.poster).into(imageViewMoviePoster)

    }
    private fun fetchData()
    {
        val jsonOrder = arguments?.getString("order")
        if (!jsonOrder.isNullOrEmpty()) {
            val gson = Gson()
            order = gson.fromJson(jsonOrder, Order::class.java)
            // Bây giờ bạn đã có đối tượng order từ chuỗi JSON
        }
        val movieId =  arguments?.getInt("movieId")
        val cinemaId = arguments?.getInt("cinemaId")
        val roomId = arguments?.getString("roomId")
        textViewCinemaRoom.setText("Room "+roomId)
        Log.d("MovieId",movieId.toString())
        val call = movieService.getMovieByID(movieId!!)
        Log.i("testneh","aaaaaaaaaaaaaaaaaaa")

        call.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                Log.i("testneh","aaaaaaaaaaaaaaaaaaa")

                if (response.isSuccessful) {
                    // Handle successful response
                    Log.i("testneh","aaaaaaaaaaaaaaaaaaa")
                    movie = response.body()!!
                    Log.i("MovieData",response.body().toString())

                    setData()
                } else {
                    val errorMessage = response.message()
                    Log.i("APIMovie", errorMessage)
                    Log.i("APIMovie", "GET FAILED")
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.i("APIMovie", t.message!!)
            }
        })
        val callCinema = cinemaService.getCinemaById(cinemaId!!)
        callCinema.enqueue(object : Callback<Cinema> {
            override fun onResponse(call: Call<Cinema>, response: Response<Cinema>) {
                Log.i("testneh", "aaaaaaaaaaaaaaaaaaa")

                if (response.isSuccessful) {
                    // Handle successful response
                    Log.i("testneh", "aaaaaaaaaaaaaaaaaaa")
                    cinema = response.body()!!
                    textViewCinemaName.setText(cinema?.name)
                    Log.i("MovieData", response.body().toString())
                } else {
                    val errorMessage = response.message()
                    Log.i("APIMovie", errorMessage)
                    Log.i("APIMovie", "GET FAILED")
                }
            }
            override fun onFailure(call: Call<Cinema>, t: Throwable) {
                Log.i("APIMovie", t.message!!)
            }

        })


        val callFood = foodService.getFoodByOrder(order.orderId)
        callFood.enqueue(object : Callback<List<FoodPayment>> {
            override fun onResponse(call: Call<List<FoodPayment>>, response: Response<List<FoodPayment>>) {
                Log.i("testneh", "aaaaaaaaaaaaaaaaaaa")

                if (response.isSuccessful) {
                    // Handle successful response
                    Log.i("testneh", "aaaaaaaaaaaaaaaaaaa")
                    ListFood = response.body()!!
                    if(ListFood.count()>0)
                    {
                        val chuoiKetQua = ListFood.joinToString(separator = ", ") { foodPayment ->
                            "${foodPayment.name}x${foodPayment.quantity}"
                        }
                        textViewFoodAndDrink.setText(chuoiKetQua)

                    }
                    else
                    {
                        textViewFoodAndDrink.setText("")
                    }
                    FoodPaymentAdapter = RecyclerPaymentFoodAdapter(this@PaymentPreviewFragment,ListFood)
                    recyclerViewFoodAndBeverageList.adapter =FoodPaymentAdapter
                } else {
                    val errorMessage = response.message()
                    Log.i("APIMovie", errorMessage)
                    Log.i("APIMovie", "GET FAILED")
                }
            }
            override fun onFailure(call: Call<List<FoodPayment>>, t: Throwable) {
                Log.i("APIMovie", t.message!!)
            }

        })
    }
    private  fun updateStatus(status:String)
    {
        val sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)

        val token = sharedPref.getString("token", "") ?: ""
        val jsonObject = JsonObject()
        jsonObject.addProperty("orderId", order.orderId)
        jsonObject.addProperty("status", status)
        val call = orderService.updateOrderById(token,jsonObject)
        call.enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                Log.i("testneh", "aaaaaaaaaaaaaaaaaaa")

                if (response.isSuccessful) {
                    // Handle successful response
                    Log.i("updateStatus", "success")


                } else {
                    val errorMessage = response.message()
                    Log.i("APIMovieStatus", errorMessage)
                    Log.i("APIMovieStatus", "GET FAILED")
                }
            }
            override fun onFailure(call: Call<Order>, t: Throwable) {
                Log.i("APIMovieStatusFailure", t.message!!)
            }

        })
    }
    private fun ZaloPayment()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val orderApi = CreateOrder()

            try {
                val data =  orderApi.createOrder(order.total.toInt().toString())
                if(data!=null)
                {
                    Log.i("Amount", order.total.toString())
                    Log.i("dataZalo",  data.toString())

                }

                val code = data.getString("return_code")
                val subCode = data.getString("sub_return_code")

                Log.d("codeZalo", code)
                Log.d("codeZalo", subCode)


                if (code == "1") {
                    val token = data.getString("zp_trans_token")
                    Log.d("tokenZalo", token)

                    // Khởi tạo và thanh toán đơn hàng
                    ZaloPaySDK.getInstance().payOrder(
                        requireActivity(), // Activity hiện tại
                        token!!, // Token của ứng dụng
                        "demozpdk://app", // Deeplink của ứng dụng Merchant
                        object : PayOrderListener {

                            override fun onPaymentCanceled(zpTransToken: String?, appTransID: String?) {
                                // Xử lý khi người dùng hủy thanh toán
                                updateStatus("unpaid")
                                Log.d("ZaloPay", "Payment canceled")
                            }

                            // Redirect to Zalo/ZaloPay Store when zaloPayError == ZaloPayError.PAYMENT_APP_NOT_FOUND
                            override fun onPaymentError(zaloPayErrorCode: ZaloPayError?, zpTransToken: String?, appTransID: String?) {
                                updateStatus("unpaid")

                                Log.d("ZaloPay", "Payment error: ${zaloPayErrorCode?.name}")
                            }

                            override fun onPaymentSucceeded(transactionId: String, transToken: String, appTransID: String?) {
                                // Xử lý khi thanh toán thành công
                                Log.d("ZaloPay", "Payment succeeded: transactionId=$transactionId, transToken=$transToken, appTransID=$appTransID")
                                updateStatus("paid")
                                (this@PaymentPreviewFragment.activity as? MainActivity)?.addFragment(HomeFragment(),"home")


                            }
                        }
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("fetchData", "Error occurred: ${e}")
            }
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

        // VOUCHER
        voucherBtn = view.findViewById(R.id.voucherBtn)
        couponBtn = view.findViewById(R.id.couponBtn)

        voucherBtn.setOnClickListener {
            (this.activity as? MainActivity)?.addFragment(AddVoucherFragment(), "add_voucher")
        }
        couponBtn.setOnClickListener {
            (this.activity as? MainActivity)?.addFragment(AddVoucherFragment(), "add_voucher")
        }
        paymentButton = view.findViewById(R.id.continueBtn)
        paymentButton.setOnClickListener {
            // Thực hiện hoạt động mạng trong một coroutine
            ZaloPayment()
        }
        textViewMovieName = view.findViewById(R.id.textViewMovieName)
        textViewBookingDate = view.findViewById(R.id.textViewBookingDate)
        textViewBookingTime = view.findViewById(R.id.textViewBookingTime)
        textViewCinemaName = view.findViewById(R.id.textViewCinemaName)
        textViewCinemaRoom = view.findViewById(R.id.textViewCinemaRoom)
        textViewSeat = view.findViewById(R.id.textViewSeat)
        textViewFoodAndDrink = view.findViewById(R.id.textViewFoodAndDrink)
        textViewOriginalTotal = view.findViewById(R.id.textViewOriginalTotal)
        imageViewMoviePoster = view.findViewById(R.id.imageViewMoviePoster)
        textViewTicketQuantity  = view.findViewById(R.id.textViewTicketQuantity)
        textViewTicketTotal  = view.findViewById(R.id.textViewTicketTotal)
        textViewFoodAndBeverageTotal = view.findViewById(R.id.textViewFoodAndBeverageTotal)
        recyclerViewFoodAndBeverageList=view.findViewById(R.id.recyclerViewFoodAndBeverageList)
//        recyclerViewFoodAndBeverageList.addItemDecoration(BookingTimeFragment.MarginItem(20))
        textViewSubtotal = view.findViewById(R.id.textViewSubtotal)
        textViewTotal = view.findViewById(R.id.textViewTotal)

        recyclerViewFoodAndBeverageList.layoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.VERTICAL,false)

    }


}