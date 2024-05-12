package com.example.repcgv.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.google.android.material.textfield.TextInputLayout

class FindTicketFragment : Fragment()  {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var inputLayoutTicketCode: TextInputLayout
    private lateinit var editTextTicketCode: EditText

    private lateinit var findBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_find_ticket, container, false)

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

        inputLayoutTicketCode = view.findViewById(R.id.inputLayoutTicketCode)
        editTextTicketCode = view.findViewById(R.id.editTextTicketCode)

        findBtn = view.findViewById(R.id.findBtn)

        editTextTicketCode.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val ticketCode: String = s.toString().trim()
                if(ticketCode.isEmpty()){
                    inputLayoutTicketCode.error = "This field cannot be empty"
                    inputLayoutTicketCode.isErrorEnabled = true
                }
                else if (!ticketCode.matches(Regex("\\d+"))) {
                    inputLayoutTicketCode.error = "Please enter a valid ticket code"
                    inputLayoutTicketCode.isErrorEnabled = true
                }
                else{
                    inputLayoutTicketCode.error = ""
                    inputLayoutTicketCode.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        findBtn.setOnClickListener {
            if(inputLayoutTicketCode.isErrorEnabled || editTextTicketCode.text.isEmpty()){
                Toast.makeText(this.requireContext(), "You haven't completed the form yet", Toast.LENGTH_SHORT).show()
            }
            else{
                val fragment = TicketDetailFragment()
                fragment.arguments = Bundle().apply {
                    putInt("id", Integer.parseInt(editTextTicketCode.text.toString()))
                }
                (this.activity as? MainActivity)?.addFragment(fragment, "ticket_detail")
            }
        }
    }
}