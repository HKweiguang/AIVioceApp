package com.shimmer.lib_voice.engine

import android.util.Log
import com.shimmer.lib_voice.impl.OnNluResultListener
import com.shimmer.lib_voice.words.NluWords
import org.json.JSONArray
import org.json.JSONObject

object VoiceEngineAnalyze {

    private val TAG = VoiceEngineAnalyze::class.java.simpleName

    private lateinit var mOnNulResultListener: OnNluResultListener

    /**
     * 分析结果
     */
    fun analyzeNlu(nlu: JSONObject, onNluResultListener: OnNluResultListener) {
        this.mOnNulResultListener = onNluResultListener

        // 用户说的话
        val rawText = nlu.optString("raw_text")
        Log.i(TAG, "rawText: $rawText")

        val results = nlu.optJSONArray("results")
        results ?: return

        val nluResultLength = results.length()
        when  {
            nluResultLength <=0 -> {
                return
            }
            nluResultLength == 1 -> {
                // 单条命中
                analyzeNluSingle(results[0] as JSONObject)
            }
            else -> {
                // 多条命中
            }
        }
    }

    /**
     * 处理单条数据
     */
    private fun analyzeNluSingle(results: JSONObject) {
        val domain = results.optString("domain")
        val intent = results.optString("intent")
        val slots = results.optJSONObject("slots")

        when (domain) {
            NluWords.NLU_WEATHER -> {
                // 获取其他类型
            }
        }
    }
}