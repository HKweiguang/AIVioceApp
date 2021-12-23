package com.shimmer.module_developer

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.base.adapter.CommonAdapter
import com.shimmer.lib_base.base.adapter.CommonViewHolder
import com.shimmer.lib_base.helper.ARouterHelper
import com.shimmer.lib_voice.manager.VoiceManager
import com.shimmer.lib_voice.tts.VoiceTTS
import com.shimmer.module_developer.data.DeveloperListData
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

@Route(path = ARouterHelper.PATH_DEVELOPER)
class DeveloperActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_developer

    override fun getTitleText() = getString(R.string.app_title_developer)

    override fun isShowBack() = true

    private val rvDeveloperView by lazy { findViewById<RecyclerView>(R.id.rvDeveloperView) }

    companion object {
        private const val mTypeTitle = 0

        private const val mTypeContext = 1
    }

    private val mList = arrayListOf<DeveloperListData>()

    override fun initView() {

        AndPermission.with(this)
            .runtime()
            .permission(Permission.RECORD_AUDIO)
            .start()

        initData()
        initListView()
    }

    private fun initData() {
        val dataArray = resources.getStringArray(R.array.DeveloperListArray)
        dataArray.forEach {
            if (it.contains("[")) {
                addItemData(mTypeTitle, it.replace("[", "").replace("]", ""))
            } else {
                addItemData(mTypeContext, it)
            }
        }
    }

    private fun initListView() {
        rvDeveloperView.layoutManager = LinearLayoutManager(this)
        rvDeveloperView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL

            )
        )
        rvDeveloperView.adapter = CommonAdapter<DeveloperListData>(
            mList,
            object : CommonAdapter.OnMoreBindDataListener<DeveloperListData> {
                override fun onBindViewHolder(
                    model: DeveloperListData,
                    holder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    when (model.type) {
                        mTypeTitle -> {
                            holder.setText(R.id.mTvDeveloperTitle, model.text)
                        }
                        mTypeContext -> {
                            holder.setText(R.id.mTvDeveloperContent, "${position}.${model.text}")
                            holder.itemView.setOnClickListener {
                                itemClickFun(position)
                            }
                        }
                    }
                }

                override fun getLayoutId(type: Int) = if (type == mTypeTitle) {
                    R.layout.layout_developer_title
                } else {
                    R.layout.layout_developer_content
                }


                override fun getItemType(position: Int) = mList[position].type
            })
    }

    private fun addItemData(type: Int, text: String) {
        mList.add(DeveloperListData(type, text))
    }

    private fun itemClickFun(position: Int) {
        when (position) {
            1 -> ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
            2 -> ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)
            3 -> ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
            4 -> ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
            5 -> ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)
            6 -> ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
            7 -> ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)

            14 -> VoiceManager.startWakeUp()
            15 -> VoiceManager.stopWakeUp()

            20 -> VoiceManager.ttsStart("你好", object : VoiceTTS.OnTTSResultListener {
                override fun ttsEnd() {

                }
            })
            21 -> VoiceManager.ttsPause()
            22 -> VoiceManager.ttsResume()
            23 -> VoiceManager.ttsStop()
            24 -> VoiceManager.ttsRelease()
        }
    }
}