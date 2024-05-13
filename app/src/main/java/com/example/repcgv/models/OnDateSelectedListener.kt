package com.example.repcgv.models

import android.widget.RadioButton
import java.util.Date

interface OnDateSelectedListener {
    fun onDateSelected(date: Date, radioButton: RadioButton)
}