package com.example.repcgv

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.repcgv.api.AccountApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.fragments.BookByMovieFragment
import com.example.repcgv.fragments.HomeFragment
import com.example.repcgv.fragments.LoginFragment
import com.example.repcgv.fragments.MapFragment
import com.example.repcgv.fragments.MovieManagementFragment
import com.example.repcgv.fragments.NewsAndPromosFragment
import com.example.repcgv.fragments.TicketFragment
import com.example.repcgv.fragments.UserDashboardFragment
import com.example.repcgv.fragments.VoucherRedeemFragment
import com.example.repcgv.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.zalopay.sdk.ZaloPaySDK

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var frameLayout: FrameLayout
    private lateinit var homeBtn: Button
    private lateinit var memberBtn: Button
    private lateinit var bookByMovieBtn: Button
    private lateinit var newsBtn: Button
    private lateinit var mapBtn: Button
    private lateinit var redeemBtn: Button

    private lateinit var ticketBtn: Button

    private lateinit var manageScheduleBtn: Button
    private lateinit var manageMoviesBtn: Button

    var isLoggedIn: Boolean = true
    var isAdmin: Boolean = false
    private lateinit var loginBtn: TextView
    private lateinit var userName: TextView
    private lateinit var memberCode: TextView
    private lateinit var userCode: CardView
    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        addButtonClickEvent()
        toggleNavbarUser()
    }

    fun init(){
        drawerLayout = findViewById(R.id.drawerLayout)
        frameLayout = findViewById(R.id.frame_layout)
        homeBtn = findViewById(R.id.home_btn)
        memberBtn = findViewById(R.id.user_btn)
        bookByMovieBtn = findViewById(R.id.book_by_movie_btn)
        newsBtn = findViewById(R.id.news_btn)
        mapBtn = findViewById(R.id.map_btn)
        redeemBtn = findViewById(R.id.redeem_btn)

        ticketBtn = findViewById(R.id.ticket_btn)

        manageScheduleBtn = findViewById(R.id.manage_schedule_btn)
        manageMoviesBtn = findViewById(R.id.manage_movies_btn)

        loginBtn = findViewById(R.id.login_btn)
        userName = findViewById(R.id.user_name)
        memberCode = findViewById(R.id.member_code)
        userCode = findViewById(R.id.user_code)
        logoutBtn = findViewById(R.id.logout_btn)

        supportFragmentManager.addOnBackStackChangedListener {
            Log.i("meo", "${supportFragmentManager.backStackEntryCount}")
        }

        replaceFragment(HomeFragment(), "home")
    }

    fun addButtonClickEvent(){
        homeBtn.setOnClickListener {
            addFragment(HomeFragment(), "home")
        }

        memberBtn.setOnClickListener {
            val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
            val token = sharedPref.getString("token", "") ?: ""
            if (token == "") {
                Toast.makeText(applicationContext, "You need to log in to use this feature!", Toast.LENGTH_SHORT).show()
                addFragment(LoginFragment(), "login")
            }
            else {
                addFragment(UserDashboardFragment(), "member")
            }
        }

        bookByMovieBtn.setOnClickListener {
            addFragment(BookByMovieFragment(), "book_by_movie")
        }

        newsBtn.setOnClickListener {
            addFragment(NewsAndPromosFragment(), "news")
        }

        mapBtn.setOnClickListener {
            addFragment(MapFragment(), "map")
        }

        redeemBtn.setOnClickListener {
            val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
            val token = sharedPref.getString("token", "") ?: ""
            if (token == "") {
                Toast.makeText(applicationContext, "You need to log in to use this feature!", Toast.LENGTH_SHORT).show()
                addFragment(LoginFragment(), "login")
            }
            else {
                addFragment(VoucherRedeemFragment(), "redeem")
            }
        }

        ticketBtn.setOnClickListener {
            val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
            val token = sharedPref.getString("token", "") ?: ""
            if (token == "") {
                Toast.makeText(applicationContext, "You need to log in to use this feature!", Toast.LENGTH_SHORT).show()
                addFragment(LoginFragment(), "login")
            }
            else {
                addFragment(TicketFragment(), "ticket")
            }
        }

        manageMoviesBtn.setOnClickListener {
            addFragment(MovieManagementFragment(), "movie_management")
        }

        manageScheduleBtn.setOnClickListener {
            //addFragment(ScheduleManagementFragment(), "schedule_management")
        }

        loginBtn.setOnClickListener {
            addFragment(LoginFragment(), "login")
        }

        logoutBtn.setOnClickListener {
            val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.remove("token")
            editor.apply()
            toggleNavbarUser()
            manageScheduleBtn.visibility = View.GONE
            manageMoviesBtn.visibility = View.GONE
            addFragment(HomeFragment(), "home")
        }
    }

    fun openDrawer(){
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private fun getCurrentFragmentName(): String? {
        val fragmentManager = supportFragmentManager
        val backStackCount = fragmentManager.backStackEntryCount

        if (backStackCount > 0) {
            // Get the name of the top entry in the back stack
            val topEntry = fragmentManager.getBackStackEntryAt(backStackCount - 1)
            return topEntry.name
        }

        // No entry in the back stack
        return null
    }

    private fun replaceFragment(fragment: Fragment, name: String? = null){
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
        fragmentTransaction.replace(frameLayout.id, fragment)
        fragmentTransaction.addToBackStack(name)

        fragmentTransaction.commit()

        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

    private fun clearAllFragments(){
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


    fun addFragment(fragment: Fragment, name: String? = null){
        val fragmentName = getCurrentFragmentName()
        if(fragmentName != name) {
            if(name == "home"){
                clearAllFragments()
                replaceFragment(HomeFragment(), "home")
            }
            else{
                replaceFragment(fragment, name)
            }
        }
    }

    fun goBack(){
        val fragmentManager: FragmentManager = supportFragmentManager
        if(fragmentManager.backStackEntryCount > 1){
            fragmentManager.popBackStack()
        }
        else{
            finish()
        }
    }

    override fun onBackPressed() {
        val fragmentManager: FragmentManager = supportFragmentManager
        if(fragmentManager.backStackEntryCount > 1){
            fragmentManager.popBackStack()
        }
        else if(fragmentManager.backStackEntryCount == 1){
            finish()
        }
        else{
            super.onBackPressed()
        }

    }

    fun toggleNavbarUser(){
        val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""
        isLoggedIn = token != ""

        if(token != ""){
            val accountService = RetrofitClient.instance.create(AccountApi::class.java)
            val call = accountService.getUserDetail(token)
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val user = response.body()!!
                        userName.text = user.name
                        isAdmin = user.role == "admin"

                        manageScheduleBtn.visibility = if(isLoggedIn && isAdmin) View.VISIBLE else View.GONE
                        manageMoviesBtn.visibility = if(isLoggedIn && isAdmin) View.VISIBLE else View.GONE
                    } else {
                        if(response.code() == 401){
                            Toast.makeText(this@MainActivity.applicationContext, "Token expired, please log in again.", Toast.LENGTH_SHORT).show()
                            val editor = sharedPref.edit()
                            editor.remove("token")
                            editor.apply()
                            addFragment(LoginFragment(), "login")
                        }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
        }

        loginBtn.visibility = if(isLoggedIn) View.GONE else View.VISIBLE
        userName.visibility = if(isLoggedIn) View.VISIBLE else View.GONE
        memberCode.visibility = if(isLoggedIn) View.VISIBLE else View.GONE
        userCode.visibility = if(isLoggedIn) View.VISIBLE else View.GONE
        logoutBtn.visibility = if(isLoggedIn) View.VISIBLE else View.GONE
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}