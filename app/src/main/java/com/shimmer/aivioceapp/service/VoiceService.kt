package com.shimmer.aivioceapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.VoicemailContract
import com.shimmer.lib_base.helper.NotificationHelper
import com.shimmer.lib_base.utils.L
import com.shimmer.lib_voice.impl.OnAsrResultListener
import com.shimmer.lib_voice.manager.VoiceManager
import org.json.JSONObject

class VoiceService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        L.i("语音服务启动")
        initCoreVoiceService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bindNotification()
        return START_STICKY_COMPATIBILITY
    }

    private fun bindNotification() {
        startForeground(981227, NotificationHelper.bindVoiceService("正在运行"))
    }

    // 初始化语音服务
    private fun initCoreVoiceService() {
        VoiceManager.initManager(this, object : OnAsrResultListener{
            override fun wakeUpReady() {
                L.i("唤醒准备就绪")
                VoiceManager.ttsStart("唤醒引擎准备就绪")
            }

            override fun asrStartSpeak() {
                L.i("开始说话")
            }

            override fun asrStopSpeak() {
                L.i("结束说话")
            }

            override fun wakeUpSuccess(result: JSONObject) {
                L.i("唤醒成功：$result")
                // 当唤醒词是小爱同学的时候，才开始识别
            }

            override fun asrResult(result: JSONObject, nlu: String) {
                L.i("识别结果：$result : $nlu" +
                        "")
            }

            override fun voiceError(text: String) {
                L.e("发生错误：$text")
            }
        })
    }
}