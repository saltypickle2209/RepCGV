package com.example.repcgv.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class SeatButton : AppCompatButton {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                return false
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun doSomething() {
        Toast.makeText(context, "did something", Toast.LENGTH_SHORT).show()
    }
}