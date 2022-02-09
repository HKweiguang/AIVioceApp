package com.shimmer.aivioceapp.adapter

import com.shimmer.module_weather.tools.WeatherIconTools
import com.shimmer.aivioceapp.R
import com.shimmer.aivioceapp.data.ChatList
import com.shimmer.aivioceapp.entity.AppConstants
import com.shimmer.lib_base.base.adapter.CommonAdapter
import com.shimmer.lib_base.base.adapter.CommonViewHolder

class ChatListAdapter(
    mList: List<ChatList>,
) : CommonAdapter<ChatList>(mList, object : OnMoreBindDataListener<ChatList> {
    override fun onBindViewHolder(
        model: ChatList,
        holder: CommonViewHolder,
        type: Int,
        position: Int
    ) {
        when (model.type) {
            AppConstants.TYPE_MINE_TEXT -> holder.setText(R.id.tv_mine_text, model.text)
            AppConstants.TYPE_AI_TEXT -> holder.setText(R.id.tv_ai_text, model.text)
            AppConstants.TYPE_AI_WEATHER -> {
                holder.setText(R.id.tv_city, model.city)
                holder.setText(R.id.tv_info, model.info)
                holder.setText(R.id.tv_temperature, model.temperature)
                holder.setImageResource(R.id.iv_icon, WeatherIconTools.getIcon(model.wid))
            }
        }
    }

    override fun getLayoutId(type: Int): Int {
        return when (type) {
            AppConstants.TYPE_MINE_TEXT -> R.layout.layout_mine_text
            AppConstants.TYPE_AI_TEXT -> R.layout.layout_ai_text
            AppConstants.TYPE_AI_WEATHER -> R.layout.layout_ai_weather
            else -> 0
        }
    }

    override fun getItemType(position: Int) = mList[position].type

})