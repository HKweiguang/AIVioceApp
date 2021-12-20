package com.shimmer.lib_base.base

import android.app.Application
import com.shimmer.lib_base.helper.ARouterHelper
import com.shimmer.lib_base.utils.SpUtils

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ARouterHelper.initHelper(this)
        SpUtils.initUtils(this)
    }
}