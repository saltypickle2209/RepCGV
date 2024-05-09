package com.example.repcgv.fragments

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.repcgv.R
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var editTextUserEmail : EditText
    private  lateinit var editTextPassword : EditText
    private lateinit var buttonLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }
    fun login(email: String, password: String) {
        // Tạo một đối tượng JsonObject chứa email và password
        val requestJson = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }

        // Gọi phương thức authenticate từ AuthService
//        val authService = RetrofitClient.instance.create(AuthService::class.java)
//        val call = authService.authenticate(requestJson)
//        call.enqueue(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                if (response.isSuccessful) {
//                    val responseObject = response.body()
//
//                    val token = responseObject?.token
//                    if (!token.isNullOrEmpty()) {
//                        // Token đã được nhận về thành công
//                        Log.d("Login", "Received token: $token")
//
//                        // Thực hiện các hành động tiếp theo sau khi đăng nhập thành công
//                        val sharedPref = this@LoginFragment.requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
//                        val editor = sharedPref.edit()
//                        editor.putString("token", "Bearer $token")
//                        editor.apply()
//                        getAllUnusedTicket("Bearer $token")
//
//                    } else {
//                        Log.e("Login", "Token is null or empty")
//                    }
//                } else {
//                    // Xử lý khi có lỗi từ server
//                    // Log.e("Login", "Error: ${response.code()} - ${response.message()}")
//                    Toast.makeText(requireContext(), "Error: ${response.code()} - ${ response.errorBody()?.string() }", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                // Xử lý khi gặp lỗi kết nối
//                // Log.e("Login", "Failed to connect: ${t.message}")
//                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val forgotPasswordTextView = view.findViewById<TextView>(R.id.forgotPasswordTextView)
        forgotPasswordTextView.paintFlags = forgotPasswordTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        val btnRegister = view.findViewById<Button>(R.id.button2)
        btnRegister.setOnClickListener {
            //(this.activity as MainActivity).addFragment(RegisterFragment(),"register")
        }
        editTextUserEmail = view.findViewById(R.id.editTextUserEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        buttonLogin = view.findViewById(R.id.button)
        buttonLogin.setOnClickListener {
            val email = editTextUserEmail.text.toString()
            val password = editTextPassword.text.toString()
            login(email, password)
        }
        return view
    }


    private fun getAllUnusedTicket(token: String){
        if (token != "") {
//            val orderService = RetrofitClient.instance.create(OrderApi::class.java)
//            val call = orderService.getAllUnusedOrder(token)
//
//            call.enqueue(object : Callback<List<Ticket>> {
//                override fun onResponse(
//                    call: Call<List<Ticket>>,
//                    response: Response<List<Ticket>>
//
//                ) {
//                    Log.i("API", "CCTESTTTT")
//
//                    if (response.isSuccessful) {
//                        // Handle successful response
//                        Log.i("API", "TESTTTT")
//
//                        val ticketList = response.body()!!
//                        clearAllNotificationChannels(this@LoginFragment.requireContext())
//                        createNotificationsForTickets(this@LoginFragment.requireContext(), ticketList)
//
//                        (this@LoginFragment.activity as? MainActivity)?.toggleNavbarUser()
//                        (this@LoginFragment.activity as? MainActivity)?.addFragment(HomeFragment(), "home")
//                        Log.i("API", "ATESTTTT")
//
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
//                    Log.i("API", t.message!!)
//                }
//            })
        }
    }


//    fun createNotificationsForTickets(context: Context, tickets: List<Ticket>) {
//        for (ticket in tickets) {
//            createNotificationChannel(context, ticket)
//        }
//    }

    fun clearAllNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            val existingChannels = notificationManager.notificationChannels
            for (channel in existingChannels) {
                notificationManager.deleteNotificationChannel(channel.id)
            }
        }
    }

//    fun createNotificationChannel(context: Context, ticket: Ticket) {
//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//        val date = Date(ticket.scheduleDate * 1000)
//        val formattedDate = sdf.format(date)
//
//        val scheduleDateTime = sdf.parse("${formattedDate} ${ticket.scheduleStart}")
//        val calendar = Calendar.getInstance()
//        calendar.time = scheduleDateTime
//        calendar.add(Calendar.HOUR_OF_DAY, -3)
//        val notificationTime = calendar.timeInMillis
//
//        if (notificationTime < System.currentTimeMillis()) {
//            calendar.timeInMillis = System.currentTimeMillis()
//            calendar.add(Calendar.SECOND, 5)
//        }
//
//        val channelId = "ticket_notification"
//        val channelName = "Ticket Notification"
//        val channelDescription = "Prepare to go to the cinema NOW!"
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(channelId, channelName, importance).apply {
//                description = channelDescription
//            }
//            val notificationManager = context.getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Schedule the notification
//        scheduleNotification(context, channelId, notificationTime)
//    }

    val PERMISSION_REQUEST_CODE = 101
    private fun scheduleNotification(context: Context, channelId: String, notificationTime: Long) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Ticket Notification")
            .setContentText("Your ticket is coming up!")
            .setSmallIcon(R.drawable.notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSION_REQUEST_CODE // Define a constant for request code
            )
            return
        }
        //val currentTime = Calendar.getInstance().timeInMillis
        //val notificationTimeTest = currentTime + 10 * 1000
        notificationManager.notify(notificationTime.toInt(), notification)
        Log.i("Notification", "Successfully set notification")
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied. Cannot show notification.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}