package com.shimmer.module_weather

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.mikephil.charting.data.Entry
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

    override fun initView() {
        intent.run {
            val city = getStringExtra("city")
            if (!TextUtils.isEmpty(city)) {
                loadWeather(city!!)
            } else {
                loadWeather("北京")
            }
        }
    }

    /**
     * 加载城市数据
     */
    private fun loadWeather(city: String) {
        HttpManager.run {
            queryWeather(city, object : Callback<WeatherData>{
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
//                            setLineChartData(data)
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    Toast.makeText(this@WeatherActivity, getString(R.string.text_load_fail), Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
