package com.shimmer.module_weather

import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_WEATHER)
class WeatherActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_weather

    override fun getTitleText() = getString(R.string.app_title_weather)

    override fun isShowBack() = true

    override fun initView() {
    }
}