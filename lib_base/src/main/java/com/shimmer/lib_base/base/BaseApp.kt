package com.shimmer.lib_base.base

import android.app.Application
import android.content.Intent
import com.shimmer.lib_base.helper.ARouterHelper
import com.shimmer.lib_base.helper.NotificationHelper
import com.shimmer.lib_base.service.InitService
import com.shimmer.lib_base.utils.SpUtils
import com.shimmer.lib_voice.manager.VoiceManager

open class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ARouterHelper.initHelper(this)
        NotificationHelper.initHelper(this)
        startService(Intent(this, InitService::class.java))
    }
}