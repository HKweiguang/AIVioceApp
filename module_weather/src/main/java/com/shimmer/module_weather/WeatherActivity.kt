package com.shimmer.module_weather

import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.helper.ARouterHelper
import com.shimmer.lib_network.HttpManager
import com.shimmer.lib_network.bean.WeatherData
import com.shimmer.module_weather.tools.WeatherIconTools
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Route(path = ARouterHelper.PATH_WEATHER)
class WeatherActivity : BaseActivity() {

    //当前城市
    private var currentCity = "北京"

    override fun getLayoutId() = R.layout.activity_weather

    override fun getTitleText() = getString(R.string.app_title_weather)

    override fun isShowBack() = true

    private val mInfo by lazy { findViewById<TextView>(R.id.mInfo) }
    private val mIvWid by lazy { findViewById<ImageView>(R.id.mIvWid) }
    private val mTemperature by lazy { findViewById<TextView>(R.id.mTemperature) }
    private val mHumidity by lazy { findViewById<TextView>(R.id.mHumidity) }
    private val mDirect by lazy { findViewById<TextView>(R.id.mDirect) }
    private val mPower by lazy { findViewById<TextView>(R.id.mPower) }
    private val mAqi by lazy { findViewById<TextView>(R.id.mAqi) }
    private val mLineChart by lazy { findViewById<LineChart>(R.id.mLineChart) }

    override fun initView() {
        intent.run {
            val city = getStringExtra("city")
            if (!TextUtils.isEmpty(city)) {
                currentCity = city!!
            }
        }
        initChart()
        loadWeather()
    }

    /**
     * 加载城市数据
     */
    private fun loadWeather() {
        //设置
        supportActionBar?.title = currentCity

        HttpManager.run {
            queryWeather(currentCity, object : Callback<WeatherData>{
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.error_code == 10012) {
                                //超过每日可允许的次数了
                                return
                            }
                            //填充数据
                            it.result.realtime.apply {
                                //设置天气 阴
                                mInfo.text = info
                                //设置图片
                                mIvWid.setImageResource(WeatherIconTools.getIcon(wid))
                                //设置温度
                                mTemperature.text =
                                    String.format(
                                        "%s%s",
                                        temperature,
                                        getString(R.string.app_weather_t)
                                    )
                                //设置湿度
                                mHumidity.text =
                                    String.format(
                                        "%s\t%s",
                                        getString(R.string.app_weather_humidity),
                                        humidity
                                    )
                                //设置风向
                                mDirect.text =
                                    String.format(
                                        "%s\t%s",
                                        getString(R.string.app_weather_direct),
                                        direct
                                    )
                                //设置风力
                                mPower.text =
                                    String.format(
                                        "%s\t%s",
                                        getString(R.string.app_weather_power),
                                        power
                                    )
                                //设置空气质量
                                mAqi.text = String.format(
                                    "%s\t%s",
                                    getString(R.string.app_weather_aqi),
                                    aqi
                                )
                            }

                            val data = ArrayList<Entry>()
                            //绘制图表
                            it.result.future.forEachIndexed { index, future ->
                                val temp = future.temperature.substring(0, 2)
                                data.add(Entry((index + 1).toFloat(), temp.toFloat()))
                            }
                            setLineChartData(data)
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    Toast.makeText(this@WeatherActivity, getString(R.string.text_load_fail), Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    /**
     * 初始化图表
     */
    private fun initChart() {
        // 后台绘制
        mLineChart.setDrawGridBackground(true)
        // 开启描述文本
        mLineChart.description.isEnabled = true
        mLineChart.description.text = getString(R.string.text_ui_tips)
        // 触摸手势
        mLineChart.setTouchEnabled(true)
        // 支持缩放
        mLineChart.setScaleEnabled(true)
        // 拖拽
        mLineChart.isDragEnabled = true
        // 扩展缩放
        mLineChart.setPinchZoom(true)

        // 平均线
        val limitLine = LimitLine(10f, "标记")
        limitLine.lineWidth = 4f
        limitLine.enableDashedLine(10f, 10f, 0f)
        limitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
        limitLine.textSize = 10f

        val xAxis = mLineChart.xAxis
        xAxis.enableAxisLineDashedLine(10f, 10f, 0f)
        xAxis.mAxisMaximum = 5f
        xAxis.axisMinimum = 1f

        val axisLeft = mLineChart.axisLeft
        axisLeft.enableAxisLineDashedLine(10f, 10f, 0f)
        axisLeft.mAxisMaximum = 40f
        axisLeft.axisMinimum = 20f

        mLineChart.axisRight.isEnabled = false
    }

    /**
     * 设置图表数据
     */
    private fun setLineChartData(data: ArrayList<Entry>) {
        if (mLineChart.data != null && mLineChart.data.dataSetCount > 0) {
            // 获取数据容器
            val set = mLineChart.data.getDataSetByIndex(0) as LineDataSet
            set.values = data
            mLineChart.data.notifyDataChanged()
            mLineChart.notifyDataSetChanged()
        } else {
            val set = LineDataSet(data, getString(R.string.text_chart_tips_text,currentCity))

            set.enableDashedLine(10f, 10f, 0f)
            set.setCircleColor(Color.BLACK)
            set.lineWidth = 1f
            set.circleRadius = 3f
            set.setDrawCircleHole(false)
            set.valueTextSize = 10f
            set.fillColor = Color.YELLOW

            // 设置数据
            val dataset = ArrayList<LineDataSet>()
            dataset.add(set)
            val datas = LineData(dataset as List<ILineDataSet>?)
            mLineChart.data = datas

            // x轴动画
            mLineChart.animateX(2000)
            // 刷新
            mLineChart.invalidate()
            // 页眉
            val legend = mLineChart.legend
            legend.form = Legend.LegendForm.LINE
        }
    }
}
