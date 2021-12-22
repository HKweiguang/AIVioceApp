package com.shimmer.module_voice_setting

import android.widget.Button
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.shimmer.lib_base.base.BaseActivity
import com.shimmer.lib_base.base.adapter.CommonAdapter
import com.shimmer.lib_base.base.adapter.CommonViewHolder
import com.shimmer.lib_base.helper.ARouterHelper
import com.shimmer.lib_voice.manager.VoiceManager

@Route(path = ARouterHelper.PATH_VOICE_SETTING)
class VoiceSettingActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_voice_setting

    override fun getTitleText() = getString(R.string.app_title_voice_setting)

    override fun isShowBack() = true

    private val bar_voice_speed by lazy {
        findViewById<SeekBar>(R.id.bar_voice_speed).apply {
            progress = 5
            max = 15
        }
    }

    private val bar_voice_volume by lazy {
        findViewById<SeekBar>(R.id.bar_voice_volume).apply {
            progress = 5
            max = 15
        }
    }

    private val rv_voice_people by lazy {
        findViewById<RecyclerView>(R.id.rv_voice_people).apply {
            layoutManager = LinearLayoutManager(this@VoiceSettingActivity)
        }
    }

    private val btn_test by lazy { findViewById<Button>(R.id.btn_test) }

    private val mList = arrayListOf<String>()

    private var mTtsPeopleIndex: Array<String>? = null

    override fun initView() {
        initData()
        initListener()
        initPeopleView()

        btn_test.setOnClickListener {
            VoiceManager.ttsStart("大家好，我是小爱")
        }
    }

    private fun initData() {
        val mTtsPeople = resources.getStringArray(R.array.TTSPeople)

        mTtsPeopleIndex = resources.getStringArray(R.array.TTSPeopleIndex)

        mTtsPeople.forEach {
            mList.add(it)
        }
    }

    private fun initPeopleView() {
        rv_voice_people.adapter =
            CommonAdapter<String>(mList, object : CommonAdapter.OnBindDataListener<String> {
                override fun onBindViewHolder(
                    model: String,
                    holder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    holder.setText(R.id.mTvPeopleContent, model)

                    holder.itemView.setOnClickListener {
                        mTtsPeopleIndex?.let {
                            VoiceManager.setPeople(it[position])
                        }
                    }
                }

                override fun getLayoutId(type: Int) = R.layout.layout_tts_people_list
            })
    }

    private fun initListener() {
        bar_voice_speed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bar_voice_speed.progress = progress
                VoiceManager.setVoiceSpeed(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        bar_voice_volume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bar_voice_volume.progress = progress
                VoiceManager.setVoiceVolume(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
}