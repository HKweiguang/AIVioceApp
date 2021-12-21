package com.shimmer.aivioceapp

import android.content.Intent
import com.shimmer.lib_base.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_main

    override fun getTitleText() = getString(R.string.app_name)

    override fun isShowBack() = false

    override fun initView() {
        startService(Intent(this, VoiceService::class.java))
    }
}