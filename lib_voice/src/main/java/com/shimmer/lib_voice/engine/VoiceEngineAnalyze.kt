package com.shimmer.lib_voice.engine

import android.util.Log
import com.shimmer.lib_voice.impl.OnNluResultListener
import com.shimmer.lib_voice.words.NluWords
import org.json.JSONObject

object VoiceEngineAnalyze {

    private val TAG = VoiceEngineAnalyze::class.java.simpleName

    private lateinit var mOnNluResultListener: OnNluResultListener

    /**
     * 分析结果
     */
    fun analyzeNlu(nlu: JSONObject, onNluResultListener: OnNluResultListener) {
        this.mOnNluResultListener = onNluResultListener

        // 用户说的话
        val rawText = nlu.optString("raw_text")
        Log.i(TAG, "rawText: $rawText")

        val results = nlu.optJSONArray("results") ?: return
        val nluResultLength = results.length()
        when {
            nluResultLength <= 0 -> mOnNluResultListener.aiRobot(rawText)

            // 单条命中
//            nluResultLength == 1 -> analyzeNluSingle(results[0] as JSONObject)
            else -> {
                // 多条命中
                analyzeNluSingle(results[0] as JSONObject)
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

        slots?.let {
            when (domain) {
                NluWords.NLU_APP -> {
                    when (intent) {
                        NluWords.INTENT_OPEN_APP,
                        NluWords.INTENT_UNINSTALL_APP,
                        NluWords.INTENT_UPDATE_APP,
                        NluWords.INTENT_DOWNLOAD_APP,
                        NluWords.INTENT_SEARCH_APP,
                        NluWords.INTENT_RECOMMEND_APP -> {
                            // 得到打开App的名称
                            val userAppName = it.optJSONArray("user_app_name")
                            userAppName?.let { appName ->
                                if (appName.length() > 0) {
                                    val obj = appName[0] as JSONObject
                                    val word = obj.optString("word")
                                    when (word) {
                                        NluWords.INTENT_OPEN_APP -> mOnNluResultListener.openApp(
                                            word
                                        )
                                        NluWords.INTENT_UNINSTALL_APP -> mOnNluResultListener.unInstallApp(
                                            word
                                        )
                                        else -> mOnNluResultListener.otherApp(word)
                                    }

                                } else {
                                    mOnNluResultListener.nluError()
                                }
                            }
                        }
                        else -> mOnNluResultListener.nluError()
                    }
                }
                NluWords.NLU_INSTRUCTION -> {
                    when (intent) {
                        NluWords.INTENT_RETURN -> mOnNluResultListener.back()
                        NluWords.INTENT_BACK_HOME -> mOnNluResultListener.home()
                        NluWords.INTENT_VOLUME_UP -> mOnNluResultListener.setVolumeUp()
                        NluWords.INTENT_VOLUME_DOWN -> mOnNluResultListener.setVolumeDown()
                        else -> mOnNluResultListener.nluError()
                    }
                }
                NluWords.NLU_MOVIE -> {
                    if (NluWords.INTENT_MOVIE_VOL == intent) {
                        val userD = slots.optJSONArray("user_d")
                        userD?.let { user ->
                            if (user.length() > 0) {
                                val word = (user[0] as JSONObject).optString("word")
                                when (word) {
                                    "大点" -> {
                                        mOnNluResultListener.setVolumeUp()
                                    }
                                    "小点" -> {
                                        mOnNluResultListener.setVolumeDown()
                                    }
                                    else -> mOnNluResultListener.nluError()
                                }
                            } else {
                                mOnNluResultListener.nluError()
                            }
                        }
                    } else {
                        mOnNluResultListener.nluError()
                    }
                }
                NluWords.NLU_ROBOT -> {
                    if (NluWords.INTENT_MOVIE_VOL == intent) {
                        val volumeControl = slots.optJSONArray("user_volume_control")
                        volumeControl?.let { control ->
                            val word = (control[0] as JSONObject).optString("word")
                            when (word) {
                                "大点" -> {
                                    mOnNluResultListener.setVolumeUp()
                                }
                                "小点" -> {
                                    mOnNluResultListener.setVolumeDown()
                                }
                                else -> mOnNluResultListener.nluError()
                            }
                        }
                    } else {
                        mOnNluResultListener.nluError()
                    }
                }
                NluWords.NLU_TELEPHONE -> {
                    if (NluWords.INTENT_CALL == intent) {
                        when {
                            slots.has("user_call_target") -> {
                                val callTarget = slots.optJSONArray("user_call_target")
                                callTarget?.let { target ->
                                    if (target.length() > 0) {
                                        val name = (target[0] as JSONObject).optString("word")
                                        mOnNluResultListener.callPhoneForName(name)
                                    } else {
                                        mOnNluResultListener.nluError()
                                    }
                                }
                            }
                            slots.has("user_phone_number") -> {
                                val phoneNumber = slots.optJSONArray("user_phone_number")
                                phoneNumber?.let { number ->
                                    if (number.length() > 0) {
                                        val phone = (number[0] as JSONObject).optString("word")
                                        mOnNluResultListener.callPhoneForNumber(phone)
                                    } else {
                                        mOnNluResultListener.nluError()
                                    }
                                }
                            }
                            else -> mOnNluResultListener.nluError()
                        }
                    } else {
                        mOnNluResultListener.nluError()
                    }
                }
                NluWords.NLU_JOKE -> {
                    if (NluWords.INTENT_TELL_JOKE == intent) {
                        mOnNluResultListener.playJoke()
                    } else {
                        mOnNluResultListener.jokeList()
                    }
                }
                NluWords.NLU_SEARCH, NluWords.NLU_NOVEL -> {
                    when {
                        NluWords.INTENT_SEARCH == intent -> {
                            mOnNluResultListener.playJoke()
                        }
                        NluWords.INTENT_SEARCH_NOVEL == intent -> {
                            mOnNluResultListener.playJoke()
                        }
                        else -> {
                            mOnNluResultListener.nluError()
                        }
                    }
                }
                NluWords.NLU_CONSTELL -> {
                    val consTellNameArray = slots.optJSONArray("user_constell_name")
                    consTellNameArray?.let { consTell ->
                        if (consTell.length() > 0) {
                            val wordObject = consTell[0] as JSONObject
                            val word = wordObject.optString("word")
                            when (intent) {
                                NluWords.INTENT_CONSTELL_TIME -> mOnNluResultListener.conTellTime(
                                    word
                                )
                                NluWords.INTENT_CONSTELL_INFO -> mOnNluResultListener.conTellInfo(
                                    word
                                )
                                else -> mOnNluResultListener.nluError()
                            }
                        }
                    }
                }
                NluWords.NLU_WEATHER -> {

                }
                NluWords.NLU_MAP -> {

                }
                else -> mOnNluResultListener.nluError()
            }

            it
        }
    }
}
