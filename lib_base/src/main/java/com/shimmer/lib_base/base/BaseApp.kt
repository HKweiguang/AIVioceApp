package com.shimmer.lib_base.base

import android.app.Application
import com.shimmer.lib_base.helper.ARouterHelper

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ARouterHelper.initHelper(this)
    }
}