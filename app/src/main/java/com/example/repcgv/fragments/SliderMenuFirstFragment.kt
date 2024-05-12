package com.example.repcgv.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.repcgv.MainActivity
import com.example.repcgv.R

class SliderMenuFirstFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slider_menu_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.memberBtn).setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            val token = sharedPref.getString("token", "") ?: ""
            if (token == "") {
                Toast.makeText(requireContext(), "You need to log in to use this feature!", Toast.LENGTH_SHORT).show()
                (this.activity as? MainActivity)?.addFragment(LoginFragment(), "login")
            }
            else {
                (this.activity as? MainActivity)?.addFragment(UserDashboardFragment(), "member")
            }
        }

        view.findViewById<Button>(R.id.mapBtn).setOnClickListener {
            (this.activity as? MainActivity)?.addFragment(MapFragment(), "map")
        }

        view.findViewById<Button>(R.id.ticketBtn).setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            val token = sharedPref.getString("token", "") ?: ""
            if (token == "") {
                Toast.makeText(requireContext(), "You need to log in to use this feature!", Toast.LENGTH_SHORT).show()
                (this.activity as? MainActivity)?.addFragment(LoginFragment(), "login")
            }
            else {
                (this.activity as? MainActivity)?.addFragment(TicketFragment(), "ticket")
            }
        }
    }
}