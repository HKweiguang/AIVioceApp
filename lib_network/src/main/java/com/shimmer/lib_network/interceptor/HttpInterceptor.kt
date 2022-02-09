package com.shimmer.lib_network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HttpInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // 请求参数
        val request = chain.request()
        val response = chain.proceed(request)

        return response
    }
}