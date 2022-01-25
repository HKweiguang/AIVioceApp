package com.shimmer.aivioceapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.shimmer.lib_base.helper.NotificationHelper
import com.shimmer.lib_base.utils.L
import com.shimmer.lib_voice.engine.VoiceEngineAnalyze
import com.shimmer.lib_voice.impl.OnAsrResultListener
import com.shimmer.lib_voice.impl.OnNluResultListener
import com.shimmer.lib_voice.manager.VoiceManager
import com.shimmer.lib_voice.tts.VoiceTTS
import com.shimmer.lib_voice.words.WordsTools
import org.json.JSONObject

class VoiceService : Service(), OnNluResultListener {

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
        VoiceManager.initManager(this, object : OnAsrResultListener {
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
                val errorCode = result.optInt("errorCode")
                // 唤醒成功
                if (errorCode == 0) {
                    // 唤醒词
                    val word = result.optString("word")
                    if (word == "小爱同学") {
                        VoiceManager.ttsStart(WordsTools.wakeupWords(), object : VoiceTTS.OnTTSResultListener{
                            override fun ttsEnd() {
                                VoiceManager.startAsr()
                            }
                        })
                    }
                }
            }

            override fun asrResult(result: JSONObject) {
                L.i("识别结果：$result")
            }

            override fun nluResult(nlu: JSONObject) {
                L.i("识别结果：$nlu")
                VoiceEngineAnalyze.analyzeNlu(nlu, this@VoiceService)
            }

            override fun voiceError(text: String) {
                L.e("发生错误：$text")
            }
        })
    }

    override fun queryWeather() {

    }
}