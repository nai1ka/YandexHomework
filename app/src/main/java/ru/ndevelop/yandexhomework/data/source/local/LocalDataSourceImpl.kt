package ru.ndevelop.yandexhomework.data.source.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.ndevelop.yandexhomework.core.models.TodoItem
import ru.ndevelop.yandexhomework.data.models.local.toDomainModel
import ru.ndevelop.yandexhomework.data.models.local.toLocalModel
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val todoDao: TodoDao
) : LocalDataSource {


    override fun getFlow(): Flow<List<TodoItem>> =
        todoDao.getFlowOfAll().map { it.map { localTodo -> localTodo.toDomainModel() } }
            .distinctUntilChanged()


    override suspend fun getItems(): List<TodoItem> {
        return todoDao.getAll().map { it.toDomainModel() }
    }

    override suspend fun addItem(todoItem: TodoItem) {
        todoDao.insert(todoItem.toLocalModel())
    }

    override suspend fun updateItem(todoItem: TodoItem) {
        todoDao.insert(todoItem.toLocalModel())
    }

    override suspend fun deleteItem(todoItem: TodoItem) {
        todoDao.delete(todoItem.id)
    }
}