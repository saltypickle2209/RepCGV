package com.example.repcgv.helper

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Helper {
    companion object {
        // Retrofit enqueue method to use the Fragment's lifecycle
        fun <T> Call<T>.enqueueWithLifecycle(fragment: Fragment, callback: Callback<T>) {
            fragment.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun cancelCalls() {
                    this@enqueueWithLifecycle.cancel()
                }
            })
            this.enqueue(callback)
        }
    }
}