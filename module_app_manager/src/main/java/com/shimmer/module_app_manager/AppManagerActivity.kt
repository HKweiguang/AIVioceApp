package com.shimmer.module_app_manager

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.base.adapter.BasePagerAdapter
import com.shimmer.lib_base.helper.ARouterHelper
import com.shimmer.lib_base.helper.`fun`.AppHelper
import com.shimmer.lib_base.utils.L
import com.shimmer.lib_base.view.PointLayoutView

@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManagerActivity : BaseActivity() {

    private val waitApp = 1000

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == waitApp) {
                waitAppHandler()
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_app_manager

    override fun getTitleText() = getString(R.string.app_title_app_manager)

    override fun isShowBack() = true

    private val mViewPager by lazy { findViewById<ViewPager>(R.id.mViewPager) }
    private val ll_loading by lazy { findViewById<LinearLayout>(R.id.ll_loading) }
    private val mPointLayoutView by lazy { findViewById<PointLayoutView>(R.id.mPointLayoutView) }

    override fun initView() {

        ll_loading.visibility = View.VISIBLE

        waitAppHandler()
    }

    //等待应用加载完成
    private fun waitAppHandler() {
        L.i("等待App列表刷新...")
        if (AppHelper.mAllViewList.size > 0) {
            initViewPager()
        } else {
            mHandler.sendEmptyMessageDelayed(waitApp, 1000)
        }
    }

    private fun initViewPager() {
        mViewPager.offscreenPageLimit = AppHelper.getPageSize()
        mViewPager.adapter = BasePagerAdapter(AppHelper.mAllViewList)
        ll_loading.visibility = View.GONE
        mViewPager.visibility = View.VISIBLE

        mPointLayoutView.setPointSize(AppHelper.getPageSize())

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageSelected(position: Int) {
                mPointLayoutView.setCheck(position)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}