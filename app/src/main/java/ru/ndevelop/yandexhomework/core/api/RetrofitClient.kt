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
    private val client = OkHttpClient.Builder().addInterceptor(AuthHeaderInterceptor()).build()
    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(ApiConsts.BASE_URL).addConverterFactory(
        json.asConverterFactory("application/json; charset=UTF8".toMediaType())
    ).client(client).build()

    val todoApi: TodoApi = retrofit.create(TodoApi::class.java)

    var knownRevision = 0
}