package com.example.repcgv.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.ViewPagerTicketApdater
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TicketFragment : Fragment() {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private lateinit var viewPagerAdapter: ViewPagerTicketApdater

    private lateinit var findTicketBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)

        init(view)

        return view
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

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        viewPagerAdapter = ViewPagerTicketApdater(this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "Upcoming movies"
                1 -> tab.text = "Watched movies"
                else -> tab.text = "How do you get to this?"
            }
        }.attach()

        findTicketBtn = view.findViewById(R.id.findTicketBtn)
        findTicketBtn.setOnClickListener {
            (this.activity as? MainActivity)?.addFragment(FindTicketFragment(), "find_ticket")
        }
    }
}