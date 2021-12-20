package com.shimmer.module_developer

import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_DEVELOPER)
class DeveloperActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_developer

    override fun getTitleText() = getString(R.string.app_title_developer)

    override fun isShowBack() = true

    override fun initView() {
    }
}