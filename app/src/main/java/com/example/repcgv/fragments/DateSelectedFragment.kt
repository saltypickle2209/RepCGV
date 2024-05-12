package com.example.repcgv.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.repcgv.R
import com.example.repcgv.models.OnDateSelectedListener
import java.util.Calendar
import java.util.Date


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DateSelectedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DateSelectedFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var onDateSelectedListener: OnDateSelectedListener? = null

    fun setOnDateSelectedListener(listener: OnDateSelectedListener) {
        onDateSelectedListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_date_selected, container, false)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val startDate = getStartDate()
        for (i in 0..6) {
            val radioButton = radioGroup.getChildAt(i) as? RadioButton
            if (radioButton != null) {
                radioButton.setText((startDate + i).toString())  // Thiết lập ngày cho RadioButton
            }
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Kiểm tra xem RadioButton nào đang được chọn
            val checkedRadioButton: RadioButton? = view.findViewById(checkedId)
            checkedRadioButton?.let {
                // Lấy text của RadioButton được chọn
                val selectedDate: String = it.text.toString()
                // Xử lý sự kiện dựa trên RadioButton được chọn
//                Log.d("Checked RadioButton", "Selected RadioButton: $text")
                val date: Date = convertToDayOfMonth(selectedDate.toInt())
                onDateSelectedListener?.onDateSelected(date, it)

            } ?: run {
//                Log.d("Checked RadioButton", "No RadioButton selected")
            }
        }

        return view
    }

    fun getStartDate(): Int {
        // Lấy dữ liệu về ngày từ Fragment argument
        val args = arguments

        return if (args != null && args.containsKey("start_date")) {
            args.getInt("start_date")
        } else 1
        // Giá trị mặc định nếu không có dữ liệu
    }

    fun convertToDayOfMonth(dayOfMonth: Int): Date {
        val calendar = Calendar.getInstance()
        // Set ngày được chọn
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        // Lấy ngày hiện tại của tháng và năm
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        // Set lại ngày với tháng và năm hiện tại
        calendar.set(currentYear, currentMonth, dayOfMonth)
        return calendar.time
    }

}