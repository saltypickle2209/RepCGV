package com.example.repcgv.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.adapters.RecyclerViewVoucherRedeemAdapter
import com.example.repcgv.decorators.VerticalSpacingItemDecorator
import com.example.repcgv.models.Voucher

class VoucherRedeemFragment : Fragment() {
    private lateinit var backBtn: ImageButton

    private lateinit var recyclerViewVoucherList: RecyclerView
    private lateinit var recyclerViewVoucherListAdapter: RecyclerViewVoucherRedeemAdapter
    private lateinit var voucherList: ArrayList<Voucher>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voucher_redeem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View){
        backBtn = view.findViewById(R.id.backBtn)

        backBtn.setOnClickListener {
            (this.activity as? MainActivity)?.goBack()
        }

        recyclerViewVoucherList = view.findViewById(R.id.recyclerViewVoucherList)

        voucherList = ArrayList()
        voucherList.add(Voucher(1, "Short voucher", 20, 20))
        voucherList.add(Voucher(2, "Long voucher name oh so long long long long long long longggggggg", 10, 20))
        voucherList.add(Voucher(3, "Short voucher", 100, 20))
        voucherList.add(Voucher(4, "Long voucher name oh so long long long long long long longggggggg", 1000, 20))

        recyclerViewVoucherListAdapter = RecyclerViewVoucherRedeemAdapter(this, voucherList)
        recyclerViewVoucherList.adapter = recyclerViewVoucherListAdapter
        recyclerViewVoucherList.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerViewVoucherList.addItemDecoration(VerticalSpacingItemDecorator(this.requireContext(), 50))

        recyclerViewVoucherListAdapter.onItemClick = { id ->
            Toast.makeText(this.requireContext(), "Item click $id", Toast.LENGTH_SHORT).show()
        }
    }
}