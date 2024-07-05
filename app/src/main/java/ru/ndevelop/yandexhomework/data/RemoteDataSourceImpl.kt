package ru.ndevelop.yandexhomework.data

import kotlinx.coroutines.delay
import ru.ndevelop.yandexhomework.core.TodoItem

class RemoteDataSourceImpl : RemoteDataSource {
    override suspend fun fetchData(): List<TodoItem> {
        // Simulate network request
        delay(1000)
        return emptyList()
    }
}
