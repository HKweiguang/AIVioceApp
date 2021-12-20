package com.shimmer.module_joke

import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_JOKE)
class JokeActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_joke

    override fun getTitleText() = getString(R.string.app_title_joke)

    override fun isShowBack() = true

    override fun initView() {
    }
}