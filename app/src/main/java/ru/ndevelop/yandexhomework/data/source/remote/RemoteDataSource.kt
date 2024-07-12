package ru.ndevelop.yandexhomework.data.source.remote

import ru.ndevelop.yandexhomework.core.models.TodoItem

interface RemoteDataSource {
    suspend fun getListOfItems(): List<TodoItem>

    suspend fun deleteItem(id: String, revision: Int)

    suspend fun addItem(item: TodoItem, revision: Int)

    suspend fun updateItem(item: TodoItem, revision: Int)
}