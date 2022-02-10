package com.shimmer.aivioceapp.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.shimmer.aivioceapp.R
import com.shimmer.aivioceapp.adapter.ChatListAdapter
import com.shimmer.aivioceapp.data.ChatList
import com.shimmer.aivioceapp.entity.AppConstants
import com.shimmer.lib_base.helper.NotificationHelper
import com.shimmer.lib_base.helper.SoundPoolHelper
import com.shimmer.lib_base.helper.WindowHelper
import com.shimmer.lib_base.helper.`fun`.AppHelper
import com.shimmer.lib_base.utils.L
import com.shimmer.lib_voice.engine.VoiceEngineAnalyze
import com.shimmer.lib_voice.impl.OnAsrResultListener
import com.shimmer.lib_voice.impl.OnNluResultListener
import com.shimmer.lib_voice.manager.VoiceManager
import com.shimmer.lib_voice.tts.VoiceTTS
import com.shimmer.lib_voice.words.WordsTools
import org.json.JSONObject

class VoiceService : Service(), OnNluResultListener {

    private val mHandler = Handler(Looper.getMainLooper())

    private lateinit var mFullWindowView: View
    private lateinit var mChatListView: RecyclerView
    private lateinit var mLottieView: LottieAnimationView
    private lateinit var tvVoiceTips: TextView
    private lateinit var ivCloseWindow: ImageView
    private val mList = ArrayList<ChatList>()
    private lateinit var mChatAdapter: ChatListAdapter

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        L.i("语音服务启动")
        initCoreVoiceService()

//        showWindow()
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
        WindowHelper.initHelper(this)
        mFullWindowView = WindowHelper.getView(R.layout.layout_window_item)
        mChatListView = mFullWindowView.findViewById<RecyclerView>(R.id.mChatListView)
        mLottieView = mFullWindowView.findViewById<LottieAnimationView>(R.id.mLottieView)
        ivCloseWindow = mFullWindowView.findViewById<ImageView>(R.id.ivCloseWindow)
        tvVoiceTips = mFullWindowView.findViewById<TextView>(R.id.tvVoiceTips)

        ivCloseWindow.setOnClickListener {
            hideTouchWindow()
        }

        mChatListView.layoutManager = LinearLayoutManager(this)
        mChatAdapter = ChatListAdapter(mList)
        mChatListView.adapter = mChatAdapter

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
                        wakeUpFix()
                    }
                }
            }

            override fun updateUserText(text: String) {
                upateTips(text)
            }

            override fun asrResult(result: JSONObject) {
                L.i("识别结果：$result")
            }

            override fun nluResult(nlu: JSONObject) {
                L.i("识别结果：$nlu")
                addMineText(nlu.optString("raw_text"))
                VoiceEngineAnalyze.analyzeNlu(nlu, this@VoiceService)
            }

            override fun voiceError(text: String) {
                L.e("发生错误：$text")
                hideWindow()
            }
        })
    }

    /**
     * 唤醒成功操作
     */
    private fun wakeUpFix() {
        showWindow()
        upateTips("正在聆听...")
        SoundPoolHelper.play(R.raw.record_start)
        val wakeupWords = WordsTools.wakeupWords()
        addAiText(wakeupWords, object : VoiceTTS.OnTTSResultListener {
            override fun ttsEnd() {
                //开启识别
                VoiceManager.startAsr()
            }
        })
    }

    /**
     * 显示窗口
     */
    private fun showWindow() {
        L.i("======显示窗口======")
        mLottieView.playAnimation()
        WindowHelper.show(mFullWindowView)
    }

    /**
     * 隐藏窗口
     */
    private fun hideWindow() {
        L.i("======隐藏窗口======")
        mHandler.postDelayed({
            WindowHelper.hide(mFullWindowView)
            mLottieView.pauseAnimation()
        }, 2 * 1000)
    }

    //直接隐藏窗口
    private fun hideTouchWindow() {
        L.i("======隐藏窗口======")
        WindowHelper.hide(mFullWindowView)
        mLottieView.pauseAnimation()
        SoundPoolHelper.play(R.raw.record_over)
        VoiceManager.stopAsr()
    }

    override fun openApp(appName: String) {
        if (!TextUtils.isEmpty(appName)) {
            L.i("Open App $appName")
            val isOpen = AppHelper.launcherApp(appName)
            if (isOpen) {
                addAiText(getString(R.string.text_voice_app_open, appName))
            } else {
                addAiText(getString(R.string.text_voice_app_not_open, appName))
            }
        }
        hideWindow()
    }

    override fun unInstallApp(appName: String) {
        if (!TextUtils.isEmpty(appName)) {
            L.i("unInstall App $appName")
            val isUninstall = AppHelper.unInstallApp(appName)
            if (isUninstall) {
                addAiText(getString(R.string.text_voice_app_uninstall, appName))
            } else {
                addAiText(getString(R.string.text_voice_app_not_uninstall))
            }
        }
        hideWindow()
    }

    override fun otherApp(appName: String) {
        //全部跳转应用市场
        if (!TextUtils.isEmpty(appName)) {
            val isIntent = AppHelper.launcherAppStore(appName)
            if (isIntent) {
                addAiText(getString(R.string.text_voice_app_option, appName))
            } else {
                addAiText(WordsTools.noAnswerWords())
            }
        }
        hideWindow()
    }

    override fun queryWeather() {

    }

    override fun nluError() {
        VoiceManager.ttsStart(WordsTools.noAnswerWords())
    }

    /**
     * 添加我的文本
     */
    private fun addMineText(text: String) {
        val bean = ChatList(AppConstants.TYPE_MINE_TEXT)
        bean.text = text
        baseAddItem(bean)
    }

    /**
     * 添加AI文本
     */
    private fun addAiText(text: String) {
        val bean = ChatList(AppConstants.TYPE_AI_TEXT)
        bean.text = text
        baseAddItem(bean)
        VoiceManager.ttsStart(text)
    }

    /**
     * 添加AI文本
     */
    private fun addAiText(text: String, mOnTTSResultListener: VoiceTTS.OnTTSResultListener) {
        val bean = ChatList(AppConstants.TYPE_AI_TEXT)
        bean.text = text
        baseAddItem(bean)
        VoiceManager.ttsStart(text, mOnTTSResultListener)
    }

    private fun baseAddItem(bean: ChatList) {
        mList.add(bean)
        mChatAdapter.notifyItemInserted(mList.size - 1)
    }

    /**
     * 更新提示语
     */
    private fun upateTips(text: String) {
        tvVoiceTips.text = text
    }
}
