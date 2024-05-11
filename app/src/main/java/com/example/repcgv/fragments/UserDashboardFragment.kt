package com.example.repcgv.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.api.AccountApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDashboardFragment : Fragment() {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var textViewUserName: TextView
    private lateinit var textViewUserID: TextView

    private lateinit var accountDetailBtn: Button
    private lateinit var accountPasswordBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        fetchData()
    }

    fun init(view: View){
        backBtn = view.findViewById(R.id.backBtn)
        menuBtn = view.findViewById(R.id.menuBtn)

        backBtn.setOnClickListener {
            (this.activity as? MainActivity)?.goBack()
        }

        menuBtn.setOnClickListener {
            (this.activity as? MainActivity)?.openDrawer()
        }

        textViewUserName = view.findViewById(R.id.textViewUserName)
        textViewUserID = view.findViewById(R.id.textViewUserID)

        accountDetailBtn = view.findViewById(R.id.accountDetailBtn)

        accountDetailBtn.setOnClickListener {
//            (this.activity as? MainActivity)?.addFragment(UserDetailFragment(), "user_detail")
        }

        accountPasswordBtn = view.findViewById(R.id.accountPasswordBtn)

        accountPasswordBtn.setOnClickListener {
//            (this.activity as? MainActivity)?.addFragment(ChangePasswordFragment(), "user_password")
        }
    }

    private fun fetchData(){
        val sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""

        if (token != "") {
            val accountService = RetrofitClient.instance.create(AccountApi::class.java)
            val call = accountService.getUserDetail(token)
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val user = response.body()!!
                        textViewUserName.text = user.name
                        textViewUserID.text = user.id
                    } else {
                        if(response.code() == 401){
                            Toast.makeText(requireContext(), "Token expired, please log in again.", Toast.LENGTH_SHORT).show()
                            val editor = sharedPref.edit()
                            editor.remove("token")
                            editor.apply()
                            (this@UserDashboardFragment.activity as? MainActivity)?.addFragment(LoginFragment(), "login")
                        }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
        }
    }
}