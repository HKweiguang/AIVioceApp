package com.shimmer.aivioceapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.shimmer.aivioceapp.data.MainListData
import com.shimmer.aivioceapp.service.VoiceService
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.base.adapter.BasePagerAdapter
import com.shimmer.lib_base.helper.ARouterHelper
import com.zhy.magicviewpager.transformer.ScaleInTransformer

class MainActivity : BaseActivity() {

    //不建议一股脑的申请权限 而是应该根据使用到的场景是让用户同意权限
    private val permission = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.VIBRATE,
        Manifest.permission.CAMERA
    )

    private val mList = arrayListOf<MainListData>()
    private val mListView = arrayListOf<View>()

    override fun getLayoutId() = R.layout.activity_main

    override fun getTitleText() = getString(R.string.app_name)

    override fun isShowBack() = false

    private val mViewPager by lazy { findViewById<ViewPager>(R.id.mViewPager) }

    override fun initView() {
        requestPermission()

        initPagerData()

        initPagerView()


    }

    /**
     * 初始化View
     */
    private fun initPagerView() {
        mViewPager.pageMargin = 20
        mViewPager.offscreenPageLimit = mList.size
        mViewPager.adapter = BasePagerAdapter(mListView)
        mViewPager.setPageTransformer(true, ScaleInTransformer())
    }

    /**
     * 初始化数据
     */
    @SuppressLint("Recycle")
    private fun initPagerData() {
        val title = resources.getStringArray(R.array.MainTitleArray)
        val color = resources.getIntArray(R.array.MainColorArray)
        val icon = resources.obtainTypedArray(R.array.MainIconArray)

        for ((index, value) in title.withIndex()) {
            mList.add(
                MainListData(
                    title = value,
                    icon = icon.getResourceId(index, 0),
                    color = color[index]
                )
            )
        }

        val windowHeight = windowManager.defaultDisplay.height
        mList.forEach {
            val view = View.inflate(this, R.layout.layout_main_list, null)

            val mCvMainView = view.findViewById<CardView>(R.id.mCvMainView)
            val mIvMainIcon = view.findViewById<ImageView>(R.id.mIvMainIcon)
            val mTvMainText = view.findViewById<TextView>(R.id.mTvMainText)

            mCvMainView.setBackgroundColor(it.color)
            mIvMainIcon.setImageResource(it.icon)
            mTvMainText.text = it.title

            mCvMainView.layoutParams?.let { lp ->
                lp.height = windowHeight / 5 * 3
            }

            //点击事件
            view.setOnClickListener { _ ->
                when (it.icon) {
                    R.drawable.img_main_weather -> ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)
                    R.drawable.img_mian_contell -> ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)
                    R.drawable.img_main_joke_icon -> ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
                    R.drawable.img_main_map_icon -> ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
                    R.drawable.img_main_app_manager -> ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
                    R.drawable.img_main_voice_setting -> ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
                    R.drawable.img_main_system_setting -> ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)
                    R.drawable.img_main_developer -> ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER)
                }
            }

            mListView.add(view)
        }
    }

    /**
     * 申请权限
     */
    private fun requestPermission() {
        if (checkPermission(permission)) {
            linkService()
        } else {
            requestPermission(
                permission,
                { linkService() })
        }

        if (!checkWindowPermission()) {
            requestWindowPermission(packageName)
        }
    }

    /**
     * 连接服务
     */
    private fun linkService() {
        startService(Intent(this, VoiceService::class.java))
    }
}