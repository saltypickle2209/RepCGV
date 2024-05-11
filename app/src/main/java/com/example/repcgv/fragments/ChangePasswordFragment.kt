package com.example.repcgv.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.repcgv.api.AccountApi
import com.example.repcgv.api.RetrofitClient
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class ChangePasswordFragment : Fragment() {
    private lateinit var backBtn: ImageButton
    private lateinit var menuBtn: ImageButton

    private lateinit var inputLayoutOldPassword: TextInputLayout
    private lateinit var inputLayoutNewPassword: TextInputLayout
    private lateinit var inputLayoutConfirmPassword: TextInputLayout

    private lateinit var editTextOldPassword: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var editTextConfirmPassword: EditText

    private lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
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

        inputLayoutOldPassword = view.findViewById(R.id.inputLayoutOldPassword)
        inputLayoutNewPassword = view.findViewById(R.id.inputLayoutNewPassword)
        inputLayoutConfirmPassword = view.findViewById(R.id.inputLayoutConfirmPassword)

        editTextOldPassword = view.findViewById(R.id.editTextOldPassword)
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword)
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword)

        saveBtn = view.findViewById(R.id.saveBtn)

        editTextOldPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val oldPassword: String = s.toString().trim()
                if(oldPassword.isEmpty()){
                    inputLayoutOldPassword.error = "This field cannot be empty"
                    inputLayoutOldPassword.isErrorEnabled = true
                }
                else{
                    inputLayoutOldPassword.error = ""
                    inputLayoutOldPassword.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        editTextNewPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newPassword: String = s.toString().trim()
                val pattern: Pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()-+=])[A-Za-z\\d!@#\$%^&*()-+=]+\$")
                if(newPassword.isEmpty()){
                    inputLayoutNewPassword.error = "This field cannot be empty"
                    inputLayoutNewPassword.isErrorEnabled = true
                }
                else if(newPassword.length < 6){
                    inputLayoutNewPassword.error = "The password must have at least 6 characters"
                    inputLayoutNewPassword.isErrorEnabled = true
                }
                else if(!pattern.matcher(newPassword).find()){
                    inputLayoutNewPassword.error = "The password must have at least one uppercase letter, one lowercase letter, a number and a special character (eg. [!@#\$%^&*()-+=])"
                    inputLayoutNewPassword.isErrorEnabled = true
                }
                else if(newPassword == editTextOldPassword.text.toString().trim()){
                    inputLayoutNewPassword.error = "The new password is the same with the old password"
                    inputLayoutNewPassword.isErrorEnabled = true
                }
                else{
                    inputLayoutNewPassword.error = ""
                    inputLayoutNewPassword.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        editTextConfirmPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val confirmPassword: String = s.toString().trim()
                if(confirmPassword.isEmpty()){
                    inputLayoutConfirmPassword.error = "This field cannot be empty"
                    inputLayoutConfirmPassword.isErrorEnabled = true
                }
                else if(confirmPassword != editTextNewPassword.text.toString().trim()){
                    inputLayoutConfirmPassword.error = "The confirm password doesn't match the new password"
                    inputLayoutConfirmPassword.isErrorEnabled = true
                }
                else{
                    inputLayoutConfirmPassword.error = ""
                    inputLayoutConfirmPassword.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        saveBtn.setOnClickListener {
            if(inputLayoutOldPassword.isErrorEnabled || inputLayoutNewPassword.isErrorEnabled || inputLayoutConfirmPassword.isErrorEnabled
                || editTextOldPassword.text.isEmpty() || editTextNewPassword.text.isEmpty() || editTextConfirmPassword.text.isEmpty()){
                Toast.makeText(this.requireContext(), "You haven't completed the form yet", Toast.LENGTH_SHORT).show()
            }
            else{
                val requestJson = JsonObject().apply {
                    addProperty("old_password", editTextOldPassword.text.toString())
                    addProperty("new_password", editTextNewPassword.text.toString())
                }

                val sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                val token = sharedPref.getString("token", "") ?: ""

                if (token != "") {
                    val accountService = RetrofitClient.instance.create(AccountApi::class.java)
                    val call = accountService.changeUserPassword(token, requestJson)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                // Handle successful response
                                Toast.makeText(
                                    requireContext(),
                                    "Successfully changed your password",
                                    Toast.LENGTH_SHORT
                                ).show()
                                (this@ChangePasswordFragment.activity as? MainActivity)?.goBack()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Error: ${response.code()} - ${response.errorBody()?.string()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.i("API", t.message!!)
                        }
                    })
                }
            }
        }
    }
}