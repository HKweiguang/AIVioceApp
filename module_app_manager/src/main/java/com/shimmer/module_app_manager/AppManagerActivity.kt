package com.shimmer.module_app_manager

import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper

@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManagerActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_app_manager

    override fun getTitleText() = getString(R.string.app_title_app_manager)

    override fun isShowBack() = true

    override fun initView() {
    }
}