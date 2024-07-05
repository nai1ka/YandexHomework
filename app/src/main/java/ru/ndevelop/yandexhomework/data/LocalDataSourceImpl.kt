package ru.ndevelop.yandexhomework.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.ndevelop.yandexhomework.core.TodoItem
import ru.ndevelop.yandexhomework.core.TodoItemImportance
import java.util.Date

class LocalDataSourceImpl : LocalDataSource {
    private var items = arrayListOf(
        TodoItem(
            id = "1",
            text = "Купить что-то",
            importance = TodoItemImportance.LOW,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "2",
            text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
            importance = TodoItemImportance.NORMAL,
            deadline = null,
            isCompleted = true,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "3",
            text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обращаться",
            importance = TodoItemImportance.NORMAL,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "4",
            text = "Купить что-то",
            importance = TodoItemImportance.HIGH,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null

        ),
        TodoItem(
            id = "5",
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
            importance = TodoItemImportance.LOW,
            deadline = null,
            isCompleted = true,
            creationDate = 0,
            updateDate = Date().time
        ),
        TodoItem(
            id = "6",
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus consequat aliquam ex, non molestie ligula viverra quis. Quisque in vestibulum nibh, ultrices condimentum eros.",
            importance = TodoItemImportance.NORMAL,
            deadline = Date().time + 10000,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "7",
            text = "Купить что-то",
            importance = TodoItemImportance.NORMAL,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "8",
            text = "Купить что-то",
            importance = TodoItemImportance.NORMAL,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "9",
            text = "Купить что-то",
            importance = TodoItemImportance.NORMAL,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "10",
            text = "Купить что-то",
            importance = TodoItemImportance.NORMAL,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "11",
            text = "Купить что-то",
            importance = TodoItemImportance.NORMAL,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "12",
            text = "Купить что-то",
            importance = TodoItemImportance.NORMAL,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
        TodoItem(
            id = "13",
            text = "Купить что-то",
            importance = TodoItemImportance.NORMAL,
            deadline = null,
            isCompleted = false,
            creationDate = 0,
            updateDate = null
        ),
    )
    private val dataFlow = MutableSharedFlow<List<TodoItem>>(replay = 1)

    override fun getData(): Flow<List<TodoItem>> = dataFlow
    override suspend fun loadData() {
        dataFlow.emit(items.toList())
    }

    override suspend fun addItem(todoItem: TodoItem) {
        items.add(todoItem)
        dataFlow.emit(items.toList())
    }

    override suspend fun updateItem(todoItem: TodoItem) {
        val index = items.indexOfFirst { it.id == todoItem.id }
        if (index != -1) {
            items[index] = todoItem
            dataFlow.emit(items.toList())
        }
    }

    override suspend fun deleteItem(todoItem: TodoItem) {
        items.removeAll { it.id == todoItem.id }
        dataFlow.emit(items.toList())
    }
}