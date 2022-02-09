@file:Suppress("DEPRECATION")

package com.shimmer.lib_base.service

import android.app.IntentService
import android.content.Intent
import com.shimmer.lib_base.utils.L
import com.shimmer.lib_base.utils.SpUtils
import com.shimmer.lib_voice.words.WordsTools


class InitService : IntentService(InitService::class.java.simpleName) {

    override fun onCreate() {
        super.onCreate()
        L.i("初始化开始")
    }

    override fun onHandleIntent(intent: Intent?) {
        L.i("初始化")

        SpUtils.initUtils(this)
        WordsTools.initTools(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}