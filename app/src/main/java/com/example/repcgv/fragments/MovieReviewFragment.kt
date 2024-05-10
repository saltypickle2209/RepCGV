package com.example.repcgv.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.RecyclerViewReviewListAdapter
import com.example.repcgv.models.Review

class MovieReviewFragment : Fragment() {
    private lateinit var root: View;
    private lateinit var buttonAdd: AppCompatButton;
    private lateinit var recyclerViewReviews: RecyclerView;
    private lateinit var adapter: RecyclerViewReviewListAdapter
    private lateinit var reviewList: List<Review>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_movie_review, container, false)
        init()
        return root;
    }


    private fun init()
    {
        buttonAdd = root.findViewById(R.id.buttonAdd)
        recyclerViewReviews = root.findViewById(R.id.recyclerViewReviews)

        reviewList = listOf(
            Review(1, "User123", 4, "Great product, highly recommended!"),
            Review(2, "Customer456", 5, "Excellent service, fast delivery."),
            Review(3, "Guest789", 3, "Average experience, could be better."),
            Review(4, "Client001", 2, "Disappointed with the quality."),
            Review(5, "Reviewer246", 5, "Amazing product, exceeded my expectations."),
            Review(6, "TestUser789", 4, "Very satisfied with my purchase."),
            Review(7, "CustomerABC", 3, "Decent product, but delivery took longer than expected."),
            Review(8, "BuyerXYZ", 5, "Impressed with the customer support."),
            Review(9, "NewUser789", 2, "Product arrived damaged, need a replacement."),
            Review(10, "LoyalCustomer", 4, "Good value for money.")
        )


        adapter = RecyclerViewReviewListAdapter(this,reviewList);
        recyclerViewReviews.adapter = adapter;
        recyclerViewReviews.layoutManager = LinearLayoutManager(context)

        buttonAdd.setOnClickListener {
            (this.activity as? MainActivity)?.addFragment(NewReviewFragment(),"newReview")
        }
    }


}