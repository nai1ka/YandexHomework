package ru.ndevelop.yandexhomework.data.source.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.ndevelop.yandexhomework.core.models.TodoItem
import javax.inject.Inject

class FakeLocalDataSource @Inject constructor(
) : LocalDataSource {
    override fun getFlow(): Flow<List<TodoItem>> = flow {}
    override suspend fun addItem(todoItem: TodoItem) {}

    override suspend fun updateItem(todoItem: TodoItem) {}

    override suspend fun deleteItem(todoItem: TodoItem) {}
    override suspend fun getItems(): List<TodoItem> {
        return emptyList()
    }
}