package com.shimmer.module_weather.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

class CitySelectView : View {

    // View高
    private var viewHeight = 0

    // View宽
    private var viewWidth = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    private fun initView() {

    }
}