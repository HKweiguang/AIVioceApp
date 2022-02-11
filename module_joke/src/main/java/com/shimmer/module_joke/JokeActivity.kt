package com.shimmer.module_joke

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.base.adapter.CommonAdapter
import com.shimmer.lib_base.base.adapter.CommonViewHolder
import com.shimmer.lib_base.helper.ARouterHelper
import com.shimmer.lib_base.utils.SpUtils
import com.shimmer.lib_network.HttpManager
import com.shimmer.lib_network.bean.Data
import com.shimmer.lib_network.bean.JokeListData
import com.shimmer.lib_voice.manager.VoiceManager
import com.shimmer.lib_voice.tts.VoiceTTS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Route(path = ARouterHelper.PATH_JOKE)
class JokeActivity : BaseActivity(), OnRefreshListener, OnRefreshLoadMoreListener {

    //页码
    private var currentPage = 1

    //数据源
    private val mList = ArrayList<Data>()
    private lateinit var mJokeAdapter: CommonAdapter<Data>

    //当前播放的下标
    private var currentPlayIndex = -1

    override fun getLayoutId() = R.layout.activity_joke

    override fun getTitleText() = getString(R.string.app_title_joke)

    override fun isShowBack() = true

    private val refreshLayout by lazy { findViewById<SmartRefreshLayout>(R.id.refreshLayout) }
    private val mJokeListView by lazy { findViewById<RecyclerView>(R.id.mJokeListView) }

    override fun initView() {
        initList()

        refreshLayout.autoRefresh()
    }

    /**
     * 加载数据 0:下拉 1：上拉
     */
    private fun loadData(orientation: Int) {
        HttpManager.queryJokeList(currentPage, object : Callback<JokeListData> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<JokeListData>, response: Response<JokeListData>) {
                if (orientation == 0) {
                    refreshLayout.finishRefresh()
                    mList.clear()

                    response.body()?.result?.data?.let { mList.addAll(it) }
                    //适配器要全部刷新
                    mJokeAdapter.notifyDataSetChanged()
                } else if (orientation == 1) {
                    refreshLayout.finishLoadMore()

                    //上一次的最大值
                    val positionStart = mList.size
                    response.body()?.result?.data?.let { mList.addAll(it) }
                    //局部刷新
                    mJokeAdapter.notifyItemRangeInserted(positionStart, mList.size)
                }
            }

            override fun onFailure(call: Call<JokeListData>, t: Throwable) {
                if (orientation == 0) {
                    refreshLayout.finishRefresh()
                } else if (orientation == 1) {
                    refreshLayout.finishLoadMore()
                }
            }
        })
    }

    /**
     * 初始化列表
     */
    private fun initList() {
        //刷新组件
        refreshLayout.setRefreshHeader(ClassicsHeader(this))
        refreshLayout.setRefreshFooter(ClassicsFooter(this))

        //监听
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnRefreshLoadMoreListener(this)

        mJokeListView.layoutManager = LinearLayoutManager(this)
        mJokeAdapter = CommonAdapter(mList, object : CommonAdapter.OnBindDataListener<Data> {
            override fun onBindViewHolder(
                model: Data,
                holder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                // 内容
                holder.setText(R.id.tvContent, model.content)

                //播放按钮
                val tvPlay = holder.getView(R.id.tvPlay) as TextView
                //设置文本
                tvPlay.text =
                    if (currentPlayIndex == position) getString(R.string.app_media_pause) else getString(
                        R.string.app_media_play
                    )

                tvPlay.setOnClickListener {
                    if (currentPlayIndex == position) {
                        currentPlayIndex = -1
                        VoiceManager.ttsPause()
                    } else {
                        currentPlayIndex = position
                        VoiceManager.ttsStart(model.content, object : VoiceTTS.OnTTSResultListener{
                            override fun ttsEnd() {
                                currentPlayIndex = -1
                                mJokeAdapter.notifyItemChanged(position)
                            }
                        })
                    }
                    mJokeAdapter.notifyItemChanged(position)
                }
            }

            override fun getLayoutId(type: Int) = R.layout.layout_joke_list_item
        })
        mJokeListView.adapter = mJokeAdapter
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        currentPage = 1
        loadData(0)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        currentPage++
        loadData(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        val isJoke = SpUtils.getBoolean("isJoke", true)
        if (!isJoke) {
            VoiceManager.ttsStop()
        }
    }
}