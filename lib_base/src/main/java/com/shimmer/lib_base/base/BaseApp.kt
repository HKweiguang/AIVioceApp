package com.shimmer.lib_base.base

import android.app.Application
import android.content.Intent
import android.os.Build
import com.shimmer.lib_base.helper.ARouterHelper
import com.shimmer.lib_base.helper.NotificationHelper
import com.shimmer.lib_base.helper.SoundPoolHelper
import com.shimmer.lib_base.helper.`fun`.AppHelper
import com.shimmer.lib_base.service.InitService
import com.shimmer.lib_base.utils.SpUtils
import com.shimmer.lib_voice.words.WordsTools

open class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ARouterHelper.initHelper(this)
        NotificationHelper.initHelper(this)

        SpUtils.initUtils(this)
        WordsTools.initTools(this)
        SoundPoolHelper.init(this)
        AppHelper.initHelper(this)
    }
}