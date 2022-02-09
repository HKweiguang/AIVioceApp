package com.shimmer.lib_network

import com.shimmer.lib_network.http.HttpKey
import com.shimmer.lib_network.http.HttpUrl
import com.shimmer.lib_network.impl.HttpImplService
import com.shimmer.lib_network.interceptor.HttpInterceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpManager {

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpInterceptor())
            .build()
    }

    private val retrofitWeather by lazy {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(HttpUrl.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiWeather by lazy {
        retrofitWeather.create(HttpImplService::class.java)
    }

    fun queryWeather(city: String): Call<ResponseBody> {
        return apiWeather.getWeather(city, HttpKey.WEATHER_KEY)
    }
}