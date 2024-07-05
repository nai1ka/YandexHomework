package ru.ndevelop.yandexhomework.data

import ru.ndevelop.yandexhomework.core.TodoItem

interface RemoteDataSource {
    suspend fun fetchData(): List<TodoItem>
}