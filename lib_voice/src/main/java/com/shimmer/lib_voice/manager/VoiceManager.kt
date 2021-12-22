package com.shimmer.lib_voice.manager

import android.content.Context
import com.shimmer.lib_voice.tts.VoiceTTS

object VoiceManager {

    internal const val VOICE_APP_ID = "25379774"
    internal const val VOICE_APP_KEY = "okUD6VXFkLVWITfWRYPTiyfC"
    internal const val VOICE_APP_SECRET = "XB9EyrwdwI1nrtGCSiCErALpGYsjuhHr"

    fun initManager(mContext: Context) {
        VoiceTTS.initTTS(mContext)
    }

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
}