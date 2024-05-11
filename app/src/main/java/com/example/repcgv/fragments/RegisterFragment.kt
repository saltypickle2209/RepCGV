package com.example.repcgv.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import com.example.repcgv.MainActivity
import com.example.repcgv.R
import com.example.repcgv.api.AuthService
import com.example.repcgv.api.RetrofitClient
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var editTextDateOfBirth : EditText
    private lateinit var editTextUserGender  : EditText
    private  lateinit var editTextUserName   : EditText
    private  lateinit var editTextUserPhone  : EditText
    private  lateinit var editTextUserEmail  : EditText
    private  lateinit var editTextPassword   : EditText
    private  lateinit var buttonRegister     : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_register, container, false)
        init(view)
        return  view
    }
    fun register(
        name: String,
        email: String,
        password: String,
        dateOfBirth: String,
        gender: String,
        phone: String
    ) {
        // Tạo một đối tượng JsonObject chứa thông tin đăng ký
        val requestJson = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", email)
            addProperty("password", password)
            addProperty("dateOfBirth", dateOfBirth)
            addProperty("gender", gender)
            addProperty("phone", phone)
        }

        // Gọi phương thức register từ AuthService
        val authService = RetrofitClient.instance.create(AuthService::class.java)
        val call = authService.register(requestJson)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Xử lý thành công
                    Log.d("Register", "Registration successful")
                    (this@RegisterFragment.activity as MainActivity).addFragment(LoginFragment(),"login")


                    // Thực hiện các hành động tiếp theo sau khi đăng ký thành công
                } else {
                    // Xử lý khi có lỗi từ server
                    Log.e("Register", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Xử lý khi gặp lỗi kết nối
                Log.e("Register", "Failed to connect: ${t.message}")
            }
        })
    }

    fun init(view: View) {
        editTextDateOfBirth = view.findViewById(R.id.editTextDateOfBirth)
        editTextUserGender = view.findViewById(R.id.editTextGender)
        editTextUserName = view.findViewById(R.id.editTextUserName)
        editTextUserPhone = view.findViewById(R.id.editTextUserPhone)
        editTextUserEmail = view.findViewById(R.id.editTextUserEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        buttonRegister = view.findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            // Lấy thông tin từ các EditText
            val name = editTextUserName.text.toString()
            val email = editTextUserEmail.text.toString()
            val password = editTextPassword.text.toString()
            val dateOfBirth = editTextDateOfBirth.text.toString()
            val gender = editTextUserGender.text.toString()
            val phone = editTextUserPhone.text.toString()

            // Gọi hàm register với thông tin đã lấy được
            register(name, email, password, dateOfBirth, gender, phone)
        }

        editTextDateOfBirth.setText("1/1/2024")
        editTextDateOfBirth.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDay = if (selectedDay < 10) "0${selectedDay}" else "$selectedDay"
                    val formattedMonth =
                        if (selectedMonth + 1 < 10) "0${selectedMonth + 1}" else "${selectedMonth + 1}"
                    val date = "$formattedDay/$formattedMonth/$selectedYear"
                    editTextDateOfBirth.setText(date)
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }
        editTextUserGender.setOnClickListener {
            val options = listOf<String>("Male", "Female", "Other")

            val dialogView = layoutInflater.inflate(R.layout.alert_dialog_radio_select, null)
            val dialogTitle = dialogView.findViewById<TextView>(R.id.textViewAlertDialogTitle)
            val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)

            dialogTitle.text = "Choose a gender"

            val black_color = ContextCompat.getColor(this.requireContext(), R.color.black)
            val checked_color = ContextCompat.getColor(this.requireContext(), R.color.red)
            val unchecked_color = ContextCompat.getColor(this.requireContext(), R.color.greytext)

            val colorStateList = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked)),
                intArrayOf(checked_color, unchecked_color)
            )

            for((index, option) in options.withIndex()){
                val radioButton = RadioButton(this.requireContext())
                radioButton.id = index
                radioButton.text = option
                radioButton.setTextColor(black_color)
                radioButton.layoutDirection = View.LAYOUT_DIRECTION_RTL
                radioButton.layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(R.dimen.radio_button_height)
                )
                CompoundButtonCompat.setButtonTintList(
                    radioButton,
                    colorStateList
                )
                radioGroup.addView(radioButton)
                if(option == editTextUserGender.text.toString()){
                    radioButton.isChecked = true
                }
            }

            val dialogBuilder = AlertDialog.Builder(this.requireContext())
                .setView(dialogView)
                .setCancelable(true)

            val dialog = dialogBuilder.create()

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = dialogView.findViewById<RadioButton>(checkedId)
                editTextUserGender.setText(selectedRadioButton.text)

                dialog?.dismiss()
            }

            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)
            dialog.show()
        }
    }

}