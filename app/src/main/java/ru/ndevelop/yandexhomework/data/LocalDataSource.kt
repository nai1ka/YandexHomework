package ru.ndevelop.yandexhomework.data

import kotlinx.coroutines.flow.Flow
import ru.ndevelop.yandexhomework.core.TodoItem

interface LocalDataSource {
    fun getData(): Flow<List<TodoItem>>
    suspend fun loadData()
    suspend fun addItem(todoItem: TodoItem)
    suspend fun updateItem(todoItem: TodoItem)
    suspend fun deleteItem(todoItem: TodoItem)
}