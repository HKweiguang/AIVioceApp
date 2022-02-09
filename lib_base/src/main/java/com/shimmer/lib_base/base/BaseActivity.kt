package com.shimmer.lib_base.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission

abstract class BaseActivity : AppCompatActivity() {

    protected val codeWindowRequest = 1000

    abstract fun getLayoutId(): Int

    abstract fun getTitleText(): String

    abstract fun isShowBack(): Boolean

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar?.let {
                it.title = getTitleText()
                it.setDisplayHomeAsUpEnabled(isShowBack())
                it.elevation = 0F
            }
        }

        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    /**
     * 检查权限
     */
    protected fun checkPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    /**
     * 检查多个权限
     */
    protected fun checkPermission(permission: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission.forEach {
                if (checkSelfPermission(it) == PackageManager.PERMISSION_DENIED) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 检查窗口权限
     */
    protected fun checkWindowPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }

    /**
     * 申请权限
     */
    protected fun requestWindowPermission(packageName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivityForResult(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${packageName}")
                ), codeWindowRequest
            )
        }
    }

    /**
     * 请求权限
     */
    protected fun requestPermission(permission: Array<String>, granted: Action<List<String>>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(this)
                .runtime()
                .permission(permission)
                .onGranted(granted)
                .start()
        }
    }
}