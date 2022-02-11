package com.shimmer.lib_network

import com.shimmer.lib_network.bean.JokeListData
import com.shimmer.lib_network.bean.JokeOneData
import com.shimmer.lib_network.http.HttpKey
import com.shimmer.lib_network.http.HttpUrl
import com.shimmer.lib_network.impl.HttpImplService
import com.shimmer.lib_network.interceptor.HttpInterceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpManager {

    private const val PAGE_SIZE = 20

    const val JSON = "Content-type:application/json;charset=UTF-8"

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpInterceptor())
            .build()
    }

    //============================笑话============================

    //笑话对象
    private val retrofitJoke by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.JOKE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //笑话接口对象
    private val apiJoke by lazy {
        retrofitJoke.create(HttpImplService::class.java)
    }

    //查询笑话
    fun queryJoke(callback: Callback<JokeOneData>) {
        apiJoke.queryJoke(HttpKey.JOKE_KEY).enqueue(callback)
    }

    //查询笑话列表
    fun queryJokeList(page: Int, callback: Callback<JokeListData>) {
        apiJoke.queryJokeList(HttpKey.JOKE_KEY, page, PAGE_SIZE).enqueue(callback)
    }

    //============================笑话============================

    private val retrofitWeather by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //============================天气============================

    private val apiWeather by lazy {
        retrofitWeather.create(HttpImplService::class.java)
    }

    fun queryWeather(city: String): Call<ResponseBody> {
        return apiWeather.getWeather(city, HttpKey.WEATHER_KEY)
    }

    //============================天气============================
}