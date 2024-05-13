package com.example.repcgv.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.RecyclerViewMovieAdapter
import com.example.repcgv.adapters.RecyclerViewScheduleAdapter
import com.example.repcgv.api.MovieApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.api.ScheduleApi
import com.example.repcgv.helper.Helper.Companion.enqueueWithLifecycle
import com.example.repcgv.models.Movie
import com.example.repcgv.models.Schedule
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleManagementFragment : Fragment() {

    private lateinit var root: View;
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var recyclerViewScheduleList: RecyclerView
    private lateinit var adapter: RecyclerViewScheduleAdapter

    private var scheduleList: ArrayList<Schedule> = ArrayList()

    private lateinit var addScheduleButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_schedule_management, container, false)
        init()
        fetchData()
        return root
    }

    private fun fetchData(){
        val scheduleService = RetrofitClient.instance.create(ScheduleApi::class.java)

        scheduleService.GetAllSchedule().enqueueWithLifecycle(this@ScheduleManagementFragment ,object : Callback<List<Schedule>> {
            override fun onResponse(call: Call<List<Schedule>>, response: Response<List<Schedule>>) {
                if (response.isSuccessful) {
                    adapter.updateList(ArrayList(response.body()!!))
                } else {
                    val errorMessage = response.message()
                    Log.i("API", errorMessage)
                    Log.i("API", "GET FAILED")
                }
            }

            override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                Log.i("API", t.message!!)
            }
        })
    }

    private fun init(){
        backBtn = root.findViewById(R.id.backBtn)
        menuBtn = root.findViewById(R.id.menuBtn)

        backBtn.setOnClickListener {
            (this.activity as? MainActivity)?.goBack()
        }

        menuBtn.setOnClickListener {
            (this.activity as? MainActivity)?.openDrawer()
        }


        recyclerViewScheduleList = root.findViewById(R.id.recyclerViewScheduleList)

        addScheduleButton = root.findViewById(R.id.addScheduleBtn)

        addScheduleButton.setOnClickListener {
            val fragment = ScheduleEditFragment()
            fragment.arguments = Bundle().apply {
                putString("type", "add")
            }
            (this.activity as? MainActivity)?.addFragment(fragment, "schedule_edit")
        }


        adapter = RecyclerViewScheduleAdapter(this, scheduleList)
        recyclerViewScheduleList.adapter = adapter
        recyclerViewScheduleList.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration: DividerItemDecoration = DividerItemDecoration(recyclerViewScheduleList.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.item_divider)!!)
        recyclerViewScheduleList.addItemDecoration(dividerItemDecoration)

        adapter.onItemClick = {id ->
            val fragment = ScheduleEditFragment()
            fragment.arguments = Bundle().apply {
                putString("type", "edit")
                putInt("id", id)
            }
            (this.activity as? MainActivity)?.addFragment(fragment, "schedule_edit")
        }
    }

}