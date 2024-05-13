package com.example.repcgv.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.RecyclerViewTicketAdapter
import com.example.repcgv.api.AccountApi
import com.example.repcgv.api.OrderApi
import com.example.repcgv.api.RetrofitClient
import com.example.repcgv.decorators.SpacingItemDecorator
import com.example.repcgv.decorators.VerticalSpacingItemDecorator
import com.example.repcgv.models.Ticket
import com.example.repcgv.models.User
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

        if (token != "") {
            val orderService = RetrofitClient.instance.create(OrderApi::class.java)
            var call = orderService.getAllUsedOrder(token)
            arguments?.takeIf { it.containsKey("type") }?.apply {
                if(getString("type") == "unused"){
                    call = orderService.getAllUnusedOrder(token)
                }
            }

            call.enqueue(object : Callback<List<Ticket>> {
                override fun onResponse(
                    call: Call<List<Ticket>>,
                    response: Response<List<Ticket>>
                ) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        ticketList = ArrayList(response.body()!!)
                        initRecyclerView()
                    } else {
                        if (response.code() == 401) {
                            Toast.makeText(
                                requireContext(),
                                "Token expired, please log in again.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val editor = sharedPref.edit()
                            editor.remove("token")
                            editor.apply()
                            (this@TicketListViewFragment.activity as? MainActivity)?.addFragment(
                                LoginFragment(),
                                "login"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                    Log.i("API", t.message!!)
                }
            })
        }

    }

    private fun initRecyclerView(){
        recyclerViewAdapter = RecyclerViewTicketAdapter(this, ticketList)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(VerticalSpacingItemDecorator(this.requireContext(), 24))

        recyclerViewAdapter.onItemClick = {id ->
            val fragment = TicketDetailFragment()
            fragment.arguments = Bundle().apply {
                putInt("id", id)
            }
            (this.activity as? MainActivity)?.addFragment(fragment, "ticket_detail")
        }
    }
}