package com.shimmer.lib_voice.tts

import android.content.Context
import android.util.Log
import com.baidu.tts.client.SpeechError
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode
import com.shimmer.lib_voice.manager.VoiceManager

object VoiceTTS : SpeechSynthesizerListener {

    private val TAG = VoiceTTS::class.java.simpleName

    private lateinit var mSpeechSynthesizer: SpeechSynthesizer

    private var onTTSResultListener: OnTTSResultListener? = null

    internal fun initTTS(mContext: Context) {
        mSpeechSynthesizer = SpeechSynthesizer.getInstance()
        mSpeechSynthesizer.setContext(mContext)
        mSpeechSynthesizer.setAppId(VoiceManager.VOICE_APP_ID)
        mSpeechSynthesizer.setApiKey(VoiceManager.VOICE_APP_KEY, VoiceManager.VOICE_APP_SECRET)

        mSpeechSynthesizer.setSpeechSynthesizerListener(this)

        setPeople("0")
        setVoiceSpeed("5")
        setVoiceVolume("5")

        mSpeechSynthesizer.initTts(TtsMode.ONLINE)
        Log.i(TAG, "TTS init")
    }

    /**
     * 设置发音人
     */
    fun setPeople(people: String) {
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, people)
    }

    /**
     * 设置语速
     */
    fun setVoiceSpeed(speed: String) {
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, speed)
    }

    /**
     * 设置音量
     */
    fun setVoiceVolume(volume: String) {
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, volume)
    }

    override fun onSynthesizeStart(p0: String?) {
        Log.i(TAG, "合成开始")
    }

    override fun onSynthesizeFinish(p0: String?) {
        Log.i(TAG, "合成结束")
    }

    override fun onSpeechProgressChanged(p0: String?, p1: Int) {
    }

    override fun onSpeechFinish(p0: String?) {
        Log.i(TAG, "播放结束")
        onTTSResultListener?.ttsEnd()
    }

    override fun onSpeechStart(p0: String?) {
        Log.i(TAG, "开始播放")
    }

    override fun onSynthesizeDataArrived(p0: String?, p1: ByteArray?, p2: Int, p3: Int) {
    }

    override fun onError(str: String?, error: SpeechError?) {
        Log.i(TAG, "TTS 错误 $str : $error")
    }

    /**
     * 播放
     */
    internal fun start(text: String) {
        mSpeechSynthesizer.speak(text)
    }

    /**
     * 播放
     */
    internal fun start(text: String, onTTSResultListener: OnTTSResultListener) {
        mSpeechSynthesizer.speak(text)
        this.onTTSResultListener = onTTSResultListener
    }

    /**
     * 暂停
     */
    internal fun pause() {
        mSpeechSynthesizer.pause()
    }

    /**
     * 继续播放
     */
    internal fun resume() {
        mSpeechSynthesizer.resume()
    }

    /**
     * 停止播放
     */
    internal fun stop() {
        mSpeechSynthesizer.stop()
    }

    /**
     * 释放
     */
    internal fun release() {
        mSpeechSynthesizer.release()
    }

    interface OnTTSResultListener {

        fun ttsEnd()
    }
}