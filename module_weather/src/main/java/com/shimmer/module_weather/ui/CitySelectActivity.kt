package com.shimmer.module_weather.ui

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.base.adapter.CommonAdapter
import com.shimmer.lib_base.base.adapter.CommonViewHolder
import com.shimmer.lib_base.utils.AssetsUtils
import com.shimmer.module_weather.R
import com.shimmer.module_weather.data.CitySelectBean

class CitySelectActivity : BaseActivity() {

    private val mList = ArrayList<CitySelectBean>()
    private lateinit var mCitySelectAdapter: CommonAdapter<CitySelectBean>

    private val mListTitle = ArrayList<String>()

    //标题
    private val mTypeTitle = 1000

    //热门城市
    private val mTypeHotCity = 1001

    //内容
    private val mTypeContent = 1002

    override fun getLayoutId() = R.layout.activity_city_select

    override fun getTitleText() = getString(R.string.text_select_city_title)

    override fun isShowBack() = true

    private val mCityListView by lazy { findViewById<RecyclerView>(R.id.mCityListView) }

    override fun initView() {
        parsingData()
        initListView()
    }

    private fun initListView() {
        mCityListView.layoutManager = LinearLayoutManager(this)
        mCitySelectAdapter =
            CommonAdapter(mList, object : CommonAdapter.OnMoreBindDataListener<CitySelectBean> {
                override fun onBindViewHolder(
                    model: CitySelectBean,
                    holder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    when (model.type) {
                        mTypeTitle -> holder.setText(R.id.mTvCityTitle, model.title)
                        mTypeHotCity -> {
                            val mCityHotView = holder.getView(R.id.mCityHotView) as RecyclerView
                            setHotCityView(mCityHotView)
                        }
                        mTypeContent -> {
                            holder.setText(R.id.mTvCityContent, model.content)
                            holder.itemView.setOnClickListener {
                                finishResult(model.city)
                            }
                        }
                    }
                }

                override fun getLayoutId(type: Int) = when (type) {
                    mTypeTitle -> R.layout.layout_city_title
                    mTypeHotCity -> R.layout.layout_city_hot
                    mTypeContent -> R.layout.layout_city_content
                    else -> R.layout.layout_city_title
                }

                override fun getItemType(position: Int) = mList[position].type
            })
    }

    /**
     * 设置热门城市
     */
    private fun setHotCityView(mCityHotView: RecyclerView) {
        val cityList = resources.getStringArray(R.array.HotCityArray)

        mCityHotView.layoutManager = GridLayoutManager(this, 3)
        mCityHotView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mCityHotView.adapter = CommonAdapter<String>(cityList.toList(), object :
            CommonAdapter.OnBindDataListener<String> {
            override fun onBindViewHolder(
                model: String,
                holder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                holder.setText(R.id.mTvHotCity, model)

                holder.itemView.setOnClickListener {
                    finishResult(model)
                }
            }

            override fun getLayoutId(type: Int): Int {
                return R.layout.layout_hot_city_item
            }
        })
    }

    /**
     * 解析数据
     */
    private fun parsingData() {
        val city = AssetsUtils.getCity()
        addTitle("热门")
        addHot()

        city.result.forEach {
            if (!mListTitle.contains(it.province)) {
                addTitle(it.province)
            }

            // 添加内容
            addContent("${it.city}市${it.district}县", it.district, it.province)
        }
    }

    /**
     * 添加标题
     */
    private fun addTitle(title: String) {
        val data = CitySelectBean(mTypeTitle, title, "", "", title)
        mList.add(data)
        mListTitle.add(title)
    }

    /**
     * 添加内容
     */
    private fun addContent(content: String, district: String, province: String) {
        val data = CitySelectBean(mTypeContent, "", content, district, province)
        mList.add(data)
    }

    /**
     * 添加热门
     */
    private fun addHot() {
        val data = CitySelectBean(mTypeHotCity, "", "", "", getString(R.string.text_city_hot))
        mList.add(data)
    }

    private fun finishResult(city: String) {
        val intent = Intent()
        intent.putExtra("city", city)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}