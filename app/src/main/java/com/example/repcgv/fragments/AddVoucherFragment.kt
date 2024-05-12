package com.example.repcgv.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.google.android.material.textfield.TextInputLayout

class AddVoucherFragment : Fragment() {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var inputLayoutVoucherCode: TextInputLayout
    private lateinit var editTextVoucherCode: EditText

    private lateinit var addBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_voucher, container, false)

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

        inputLayoutVoucherCode = view.findViewById(R.id.inputLayoutVoucherCode)
        editTextVoucherCode = view.findViewById(R.id.editTextVoucherCode)

        addBtn = view.findViewById(R.id.addBtn)

        editTextVoucherCode.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val voucherCode: String = s.toString().trim()
                if(voucherCode.isEmpty()){
                    inputLayoutVoucherCode.error = "This field cannot be empty"
                    inputLayoutVoucherCode.isErrorEnabled = true
                }
                else{
                    inputLayoutVoucherCode.error = ""
                    inputLayoutVoucherCode.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        addBtn.setOnClickListener {
            if(inputLayoutVoucherCode.isErrorEnabled || editTextVoucherCode.text.isEmpty()){
                Toast.makeText(this.requireContext(), "You haven't completed the form yet", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this.requireContext(), "Ticket, find yourself!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}