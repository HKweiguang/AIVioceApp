package com.shimmer.lib_base.utils

import android.util.Log
import com.shimmer.lib_base.BuildConfig

object L {

    private const val TAG = "AIVioceApp"

    fun i(text: String?) {
        if (BuildConfig.DEBUG) {
            text?.let { Log.i(TAG, it) }
        }
    }

    fun e(text: String?) {
        if (BuildConfig.DEBUG) {
            text?.let { Log.e(TAG, it) }
        }
    }
}