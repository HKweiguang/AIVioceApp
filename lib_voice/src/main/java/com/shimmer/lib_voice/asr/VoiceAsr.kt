package com.shimmer.lib_voice.asr

import android.content.Context
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.shimmer.lib_voice.wakeup.VoiceWakeUp
import org.json.JSONObject

object VoiceAsr {

    // 唤醒对象
    private lateinit var asr: EventManager

    private lateinit var asrJson: String

    internal fun initAsr(mContext: Context, listener: EventListener) {
        val map = HashMap<Any, Any>()
        // 是否获取音量
        map[SpeechConstant.ACCEPT_AUDIO_VOLUME] = true
        map[SpeechConstant.ACCEPT_AUDIO_DATA] = false
        map[SpeechConstant.DISABLE_PUNCTUATION] = false
        map[SpeechConstant.PID] = 15363
//        map[SpeechConstant.PID] = 15373
        // 转换成Json
        asrJson = JSONObject(map as Map<Any, Any>).toString()

        asr = EventManagerFactory.create(mContext, "asr")
        asr.registerListener(listener)
    }

    /**
     * 开始识别
     */
    fun startAsr() {
        asr.send(SpeechConstant.ASR_START, asrJson, null, 0, 0)
    }

    /**
     * 停止识别
     */
    fun stopAsr() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0)
    }

    /**
     * 取消识别
     */
    fun cancelAsr() {
        asr.send(SpeechConstant.ASR_CANCEL, null, null, 0, 0)
    }

    /**
     * 销毁
     */
    fun releaseAsr(listener: EventListener) {
        asr.unregisterListener(listener)
    }
}