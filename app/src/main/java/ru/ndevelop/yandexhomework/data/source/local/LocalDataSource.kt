package ru.ndevelop.yandexhomework.data.source.local

import kotlinx.coroutines.flow.Flow
import ru.ndevelop.yandexhomework.core.models.TodoItem

interface LocalDataSource {
    fun getFlow(): Flow<List<TodoItem>>
    suspend fun addItem(todoItem: TodoItem)
    suspend fun updateItem(todoItem: TodoItem)
    suspend fun deleteItem(todoItem: TodoItem)
    suspend fun getItems(): List<TodoItem>
}