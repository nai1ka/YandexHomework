package ru.ndevelop.tinkoffproject.core.api

import okhttp3.Interceptor
import okhttp3.Response
import ru.ndevelop.yandexhomework.core.ApiConsts
import javax.inject.Inject

class AuthHeaderInterceptor @Inject constructor() :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithHeader = chain.request()
            .newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${ApiConsts.TOKEN}"
            )
            .build()
        return chain.proceed(requestWithHeader)
    }
}
