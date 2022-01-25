package com.shimmer.aivioceapp

import android.content.Intent
import com.shimmer.aivioceapp.service.VoiceService
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class MainActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_main

    override fun getTitleText() = getString(R.string.app_name)

    override fun isShowBack() = false

    override fun initView() {
        startService(Intent(this, VoiceService::class.java))

        AndPermission.with(this)
            .runtime()
            .permission(Permission.RECORD_AUDIO)
            .onGranted { ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER)}
            .start()
    }
}