package com.shimmer.module_weather

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_WEATHER)
class WeatherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
    }
}