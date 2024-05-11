package com.example.repcgv.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.widget.RelativeLayout


class ZoomLayout : RelativeLayout, OnScaleGestureListener {
    private enum class Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private var mode = Mode.NONE
    private var scale = 1.0f
    private var lastScaleFactor = 0f

    private var startX = 0f
    private var startY = 0f

    private var dx = 0f
    private var dy = 0f
    private var prevDx = 0f
    private var prevDy = 0f

    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels

    var childWidth = 0;
    var childHeight = 0;

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    public fun SetSize(width: Int, height: Int) {
        childWidth = width;
        childHeight = height;
    }

    fun init(context: Context?) {
        val scaleDetector = ScaleGestureDetector(context!!, this)
        setOnTouchListener { view, motionEvent ->
            val X = motionEvent!!.rawX.toInt()
            val Y = motionEvent.rawY.toInt()

            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    //Log.i(TAG, "DOWN")
                    mode = Mode.DRAG
                    startX = motionEvent.x - prevDx
                    startY = motionEvent.y - prevDy

                    val lParams = view.layoutParams as RelativeLayout.LayoutParams
                }

                MotionEvent.ACTION_MOVE -> {
                    dx = motionEvent.x - startX
                    dy = motionEvent.y - startY
                }

                MotionEvent.ACTION_POINTER_DOWN -> mode = Mode.ZOOM
                MotionEvent.ACTION_POINTER_UP -> mode = Mode.DRAG
                MotionEvent.ACTION_UP -> {
                    //Log.i(TAG, "UP")
                    mode = Mode.NONE
                    prevDx = dx
                    prevDy = dy
                }
            }
            scaleDetector.onTouchEvent(motionEvent)
            parent.requestDisallowInterceptTouchEvent(true)

            val deltaX = (childWidth - screenWidth)
            val deltaY = (childHeight - screenHeight)
            val maxDx = (childWidth - screenWidth) / 2.0f
            var maxDy = (childHeight - screenHeight) / 2.0f
            if (maxDy < 0) maxDy = 0.0f

            dx = Math.min(Math.max(dx, -maxDx - 100), maxDx + 100)
            dy = Math.min(Math.max(dy, 0.0f - 300), maxDy)
            applyScaleAndTranslation()
            view.invalidate()
            view.performClick()
            true
        }
    }

    override fun onScaleBegin(scaleDetector: ScaleGestureDetector): Boolean {
        //Log.i(TAG, "onScaleBegin")
        return true
    }

    override fun onScale(scaleDetector: ScaleGestureDetector): Boolean {
        val scaleFactor = scaleDetector.scaleFactor
        //Log.i(TAG, "onScale$scaleFactor")
        if (lastScaleFactor == 0f || Math.signum(scaleFactor) == Math.signum(lastScaleFactor)) {
            scale *= scaleFactor
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM))
            lastScaleFactor = scaleFactor
        } else {
            lastScaleFactor = 0f
        }
        return true
    }

    override fun onScaleEnd(scaleDetector: ScaleGestureDetector) {
        //Log.i(TAG, "onScaleEnd")
    }

    private fun applyScaleAndTranslation() {
        child().scaleX = scale
        child().scaleY = scale
        child().translationX = dx
        child().translationY = dy

    }

    private fun child(): View {
        if (childCount == 0) return View(context)
        return getChildAt(0)
    }

    companion object {
        private const val TAG = "ZoomLayout"
        private const val MIN_ZOOM = 1.0f
        private const val MAX_ZOOM = 1.2f
    }
}