package com.shimmer.module_map

import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_MAP)
class MapActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_map

    override fun getTitleText() = getString(R.string.app_title_map)

    override fun isShowBack() = true

    override fun initView() {
    }
}