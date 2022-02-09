package com.shimmer.lib_base.helper.`fun`

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import com.shimmer.lib_base.helper.`fun`.data.AppData

@SuppressLint("StaticFieldLeak")
object AppHelper {

    private lateinit var context: Context

    // 包管理器
    private lateinit var pm: PackageManager

    // 所有应用
    private val mAllAppList = arrayListOf<AppData>()

    fun initHelper(context: Context) {
        this.context = context

        pm = this.context.packageManager

        loadAllApp()
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
        context.startActivity(intent)
    }

    /**
     * 跳转应用商店
     */
    fun intentAppStore(packageName: String, markPackageName: String) {
        val uri = Uri.parse("market://details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(markPackageName)
        context.startActivity(intent)
    }
}