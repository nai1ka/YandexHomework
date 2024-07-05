package ru.ndevelop.yandexhomework

import android.app.Application
import android.provider.Settings
import ru.ndevelop.yandexhomework.core.api.RetrofitClient
import ru.ndevelop.yandexhomework.data.TodoItemsRepository
import ru.ndevelop.yandexhomework.data.source.local.LocalDataSourceImpl
import ru.ndevelop.yandexhomework.data.source.remote.RemoteDataSourceImpl

class App : Application() {
    val todoItemsRepository: TodoItemsRepository by lazy {
        val deviceID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        TodoItemsRepository(
            remoteDataSource = RemoteDataSourceImpl(RetrofitClient.todoApi, deviceID),
            localDataSource = LocalDataSourceImpl()
        )
    }
}