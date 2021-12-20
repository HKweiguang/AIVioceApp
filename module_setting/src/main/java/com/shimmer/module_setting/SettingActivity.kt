package com.shimmer.module_setting

import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_SETTING)
class SettingActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_setting

    override fun getTitleText() = getString(R.string.app_title_system_setting)

    override fun isShowBack() = true

    override fun initView() {
    }
}