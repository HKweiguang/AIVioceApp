package com.shimmer.module_constellation

import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_CONSTELLATION)
class ConstellationActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_constellation

    override fun getTitleText() = getString(R.string.app_title_constellation)

    override fun isShowBack() = true

    override fun initView() {
    }
}