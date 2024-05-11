package com.example.repcgv.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.RecyclerViewTicketAdapter
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.models.Ticket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketListViewFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    private lateinit var ticketList: ArrayList<Ticket>

    private lateinit var recyclerViewAdapter: RecyclerViewTicketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewTicketList)

        val sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "") ?: ""



    }

    private fun initRecyclerView(){

    }
}