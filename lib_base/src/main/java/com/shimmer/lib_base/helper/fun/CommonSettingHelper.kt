package com.shimmer.lib_base.helper.`fun`

import android.annotation.SuppressLint
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.view.KeyEvent

@SuppressLint("StaticFieldLeak")
object CommonSettingHelper {

    private lateinit var context: Context

    private lateinit var inst: Instrumentation

    fun initHelper(context: Context) {
        this.context = context

        inst = Instrumentation()
    }

    /**
     * 返回
     */
    fun back() {
        Thread({ inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK) }).start()
    }

    /**
     * 主页
     */
    fun home() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(intent)
    }

    /**
     * 音量+
     */
    fun setVolumeUp() {
        Thread({ inst.sendKeyDownUpSync(KeyEvent.KEYCODE_VOLUME_UP) }).start()

    }

    /**
     * 音量-
     */
    fun setVolumeDown() {
        Thread({ inst.sendKeyDownUpSync(KeyEvent.KEYCODE_VOLUME_DOWN) }).start()
    }
}