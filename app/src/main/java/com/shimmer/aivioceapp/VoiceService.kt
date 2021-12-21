package com.shimmer.aivioceapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.shimmer.lib_base.helper.NotificationHelper
import com.shimmer.lib_base.utils.L

class VoiceService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        L.i("语音服务启动")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bindNotification()
        return START_STICKY_COMPATIBILITY
    }

    private fun bindNotification() {
        startForeground(981227, NotificationHelper.bindVoiceService("正在运行"))
    }
}