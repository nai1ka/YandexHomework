package ru.ndevelop.yandexhomework.core.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.ndevelop.tinkoffproject.core.api.AuthHeaderInterceptor
import ru.ndevelop.tinkoffproject.core.api.TodoApi
import ru.ndevelop.yandexhomework.core.ApiConsts


object RetrofitClient {
    var knownRevision = 0
}