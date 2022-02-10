package com.shimmer.lib_base.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager

@SuppressLint("StaticFieldLeak")
object WindowHelper {

    private lateinit var mContext: Context
    private lateinit var wm: WindowManager
    private lateinit var lp: WindowManager.LayoutParams

    fun initHelper(context: Context) {
        this.mContext = context

        wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        lp = createLayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    /**
     * 创建布局属性
     */
    private fun createLayoutParams(width: Int, height: Int): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            this.width = width
            this.height = height
            gravity = Gravity.CENTER
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }
    }

    /**
     * 获取视图控件
     */
    fun getView(layoutId: Int): View {
        return View.inflate(mContext, layoutId, null)
    }

    /**
     * 显示窗口
     */
    fun show(view: View) {
        if (view.parent == null) {
            wm.addView(view, lp)
        }
    }

    /**
     * 显示窗口
     */
    fun show(view: View, lp: WindowManager.LayoutParams) {
        if (view.parent == null) {
            wm.addView(view, lp)
        }
    }

    /**
     * 隐藏视图
     */
    fun hide(view: View) {
        if (view.parent != null) {
            wm.removeView(view)
        }
    }

    /**
     * 更新视图
     */
    fun update(view: View, lp: WindowManager.LayoutParams) {
        wm.updateViewLayout(view, lp)
    }
}
