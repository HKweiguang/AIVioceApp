package com.shimmer.aivioceapp

import android.os.Bundle
import android.widget.TextView
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).apply {
            setOnClickListener {
                ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
            }
        }
    }
}