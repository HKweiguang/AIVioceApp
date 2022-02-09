package com.shimmer.lib_network.impl

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HttpImplService {

    @GET("simpleWeather/query")
    fun getWeather(
        @Query("city") city: String,
        @Query("key") key: String,
    ): Call<ResponseBody>
}