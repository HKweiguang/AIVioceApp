package com.shimmer.lib_base.helper.`fun`

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.shimmer.lib_base.R
import com.shimmer.lib_base.helper.`fun`.data.AppData

@SuppressLint("StaticFieldLeak")
object AppHelper {

    private lateinit var context: Context

    // 包管理器
    private lateinit var pm: PackageManager

    // 所有应用
    private val mAllAppList = arrayListOf<AppData>()

    //所有商店包名
    private lateinit var mAllMarkArray: Array<String>

    //分页View
    val mAllViewList = ArrayList<View>()

    fun initHelper(context: Context) {
        this.context = context

        pm = this.context.packageManager

        loadAllApp()

        // 加载商店包名
        mAllMarkArray = context.resources.getStringArray(R.array.AppMarketArray)
    }

    /**
     * 加载所有的App
     */
    @SuppressLint("QueryPermissionsNeeded")
    private fun loadAllApp() {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val appInfo = pm.queryIntentActivities(intent, 0)

        appInfo.forEach {
            mAllAppList.add(
                AppData(
                    it.activityInfo.packageName,
                    it.loadLabel(pm) as String,
                    it.loadIcon(pm),
                    it.activityInfo.name,
                    it.activityInfo.flags == ApplicationInfo.FLAG_SYSTEM
                )
            )
        }

        initPageView()
    }

    /**
     * 初始化PageView
     */
    private fun initPageView() {
        for (i in 0 until getPageSize()) {
            // -> FrameLayout
            val rootView =
                View.inflate(context, R.layout.layout_app_manager_item, null) as ViewGroup
            // -> 第一层 线性布局
            for (j in 0 until rootView.childCount) {
                // -> 第二层 六个 线性布局
                val childX = rootView.getChildAt(j) as ViewGroup
                // -> 第三层  四个线性布局
                for (k in 0 until childX.childCount) {
                    // -> 第四层  两个View ImageView TextView
                    val child = childX.getChildAt(k) as ViewGroup
                    val iv = child.getChildAt(0) as ImageView
                    val tv = child.getChildAt(1) as TextView
                    //计算你当前的下标
                    val index = i * 24 + j * 4 + k
                    if (index < mAllAppList.size) {
                        //获取数据
                        val data = mAllAppList[index]
                        tv.text = data.appName
                        iv.setImageDrawable(data.appIcon)
                        //点击事件
                        child.setOnClickListener {
                            intentApp(data.packName)
                        }
                    }
                }
            }
            mAllViewList.add(rootView)
        }
    }

    /**
     * 获取页面数量
     */
    fun getPageSize() = mAllAppList.size / 24 + 1

    /**
     * 获取非系统应用
     */
    fun getNotSystemApp(): List<AppData> {
        return mAllAppList.filter { !it.isSystemApp }
    }

    /**
     * 启动App
     */
    fun launcherApp(appName: String): Boolean {
        if (mAllAppList.size > 0) {
            mAllAppList.forEach {
                if (it.appName == appName) {
                    intentApp(it.packName)
                    return true
                }
            }
        }

        return false
    }

    /**
     * 卸载App
     */
    fun unInstallApp(appName: String): Boolean {
        if (mAllAppList.size > 0) {
            mAllAppList.forEach {
                if (it.appName == appName) {
                    intentUnInstallApp(it.packName)
                    return true
                }
            }
        }

        return false
    }

    /**
     * 启动App
     */
    private fun intentApp(packageName: String) {
        val intent = pm.getLaunchIntentForPackage(packageName)
        intent?.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(it)
        }
    }

    /**
     * 卸载App
     */
    private fun intentUnInstallApp(packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }


    /**
     * 跳转应用商店
     */
    fun launcherAppStore(appName: String): Boolean {
        mAllAppList.forEach {
            // 如果你包含，说明安装了应用商店
            if (mAllMarkArray.contains(it.packName)) {
                if (mAllAppList.size > 0) {
                    mAllAppList.forEach { data ->
                        if (data.appName == appName) {
                            intentAppStore(data.packName, data.packName)
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    /**
     * 跳转应用商店
     */
    private fun intentAppStore(packageName: String, markPackageName: String) {
        val uri = Uri.parse("market://details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(markPackageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
