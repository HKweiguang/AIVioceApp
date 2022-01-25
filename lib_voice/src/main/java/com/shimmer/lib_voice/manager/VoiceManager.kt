package com.shimmer.lib_voice.manager

import android.content.Context
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.shimmer.lib_voice.asr.VoiceAsr
import com.shimmer.lib_voice.impl.OnAsrResultListener
import com.shimmer.lib_voice.tts.VoiceTTS
import com.shimmer.lib_voice.wakeup.VoiceWakeUp
import org.json.JSONObject

object VoiceManager : EventListener {

    private val TAG = VoiceManager::class.java.simpleName

    internal const val VOICE_APP_ID = "25379774"
    internal const val VOICE_APP_KEY = "okUD6VXFkLVWITfWRYPTiyfC"
    internal const val VOICE_APP_SECRET = "XB9EyrwdwI1nrtGCSiCErALpGYsjuhHr"

    private lateinit var onAsrResultListener: OnAsrResultListener

    fun initManager(mContext: Context, onAsrResultListener: OnAsrResultListener) {
        this.onAsrResultListener = onAsrResultListener

        VoiceTTS.initTTS(mContext)
        VoiceWakeUp.initWakeUp(mContext, this)
        VoiceAsr.initAsr(mContext, this)
    }

    // TTS start------------------------------------------------------------------------------------

    /**
     * 播放
     */
    fun ttsStart(text: String, onTTSResultListener: VoiceTTS.OnTTSResultListener? = null) {
        if (onTTSResultListener != null) {
            VoiceTTS.start(text, onTTSResultListener)
        } else {
            VoiceTTS.start(text)
        }
    }

    /**
     * 暂停
     */
    fun ttsPause() {
        VoiceTTS.pause()
    }

    /**
     * 继续播放
     */
    fun ttsResume() {
        VoiceTTS.resume()
    }

    /**
     * 停止播放
     */
    fun ttsStop() {
        VoiceTTS.stop()
    }

    /**
     * 释放
     */
    fun ttsRelease() {
        VoiceTTS.release()
    }

    /**
     * 设置发音人
     */
    fun setPeople(people: String) {
        VoiceTTS.setPeople(people)
    }

    /**
     * 设置语速
     */
    fun setVoiceSpeed(speed: String) {
        VoiceTTS.setPeople(speed)
    }

    /**
     * 设置音量
     */
    fun setVoiceVolume(volume: String) {
        VoiceTTS.setPeople(volume)
    }

    // TTS end--------------------------------------------------------------------------------------

    // WakeUp start---------------------------------------------------------------------------------

    /**
     * 启动唤醒
     */
    fun startWakeUp() {
        Log.i(TAG, "启动唤醒")
        VoiceWakeUp.startWakeUp()
    }

    /**
     * 停止唤醒
     */
    fun stopWakeUp() {
        Log.i(TAG, "停止唤醒")
        VoiceWakeUp.stopWakeUp()
    }

    // WakeUp end-----------------------------------------------------------------------------------

    // ASR start------------------------------------------------------------------------------------

    /**
     * 开始识别
     */
    fun startAsr() {
        VoiceAsr.startAsr()
    }

    /**
     * 停止识别
     */
    fun stopAsr() {
        VoiceAsr.stopAsr()
    }

    /**
     * 取消识别
     */
    fun cancelAsr() {
        VoiceAsr.cancelAsr()
    }

    /**
     * 销毁
     */
    fun releaseAsr() {
        VoiceAsr.releaseAsr(this)
    }

    // ASR end--------------------------------------------------------------------------------------


    override fun onEvent(
        name: String?,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        Log.i(TAG, "event: name=$name, params=$params")

        name?.also {
            // 语音前置状态
            when (name) {
                SpeechConstant.CALLBACK_EVENT_WAKEUP_READY -> onAsrResultListener.wakeUpReady()
                SpeechConstant.CALLBACK_EVENT_ASR_BEGIN -> onAsrResultListener.asrStartSpeak()
                SpeechConstant.CALLBACK_EVENT_ASR_END -> onAsrResultListener.asrStopSpeak()
            }
            // 去除脏数据
            if (params == null) {
                return
            }

            val allJson = JSONObject(params)

            when (name) {
                SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS -> onAsrResultListener.wakeUpSuccess(
                    allJson
                )
                SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR -> onAsrResultListener.voiceError("唤醒失败")
                SpeechConstant.CALLBACK_EVENT_ASR_FINISH -> onAsrResultListener.asrResult(allJson)
                SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL -> {
                    data?.also { data ->
                        val nlu = JSONObject(String(data, offset, length))
                        onAsrResultListener.nluResult(nlu)
                    }
                }
            }
        }
    }
}