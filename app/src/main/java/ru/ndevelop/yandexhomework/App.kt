package ru.ndevelop.yandexhomework

import android.app.Application
import ru.ndevelop.yandexhomework.data.LocalDataSourceImpl
import ru.ndevelop.yandexhomework.data.RemoteDataSourceImpl
import ru.ndevelop.yandexhomework.data.TodoItemsRepository

class App: Application() {
    val todoItemsRepository: TodoItemsRepository by lazy {
        TodoItemsRepository(
            remoteDataSource = RemoteDataSourceImpl(),
            localDataSource = LocalDataSourceImpl()
        )
    }
}