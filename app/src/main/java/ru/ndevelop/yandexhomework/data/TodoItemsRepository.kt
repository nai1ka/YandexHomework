package ru.ndevelop.yandexhomework.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.ndevelop.yandexhomework.core.TodoItem


class TodoItemsRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    private val _dataStateFlow = MutableStateFlow<List<TodoItem>>(emptyList())
    val dataStateFlow: StateFlow<List<TodoItem>> = _dataStateFlow

    init {
        CoroutineScope(Dispatchers.Default).launch {
            subscribeToUpdates()
        }
    }

    suspend fun loadLocalData() {
        localDataSource.loadData()
    }

    private suspend fun subscribeToUpdates() {

        localDataSource.getData().collect { data ->
            _dataStateFlow.emit(data)
        }
    }

    suspend fun addItem(item: TodoItem) {
        localDataSource.addItem(item)
    }

    suspend fun fetchData() {
        val data = remoteDataSource.fetchData()
        _dataStateFlow.emit(data)
    }

    suspend fun updateItem(item: TodoItem) {
        localDataSource.updateItem(item)
    }

    suspend fun deleteItem(todoItem: TodoItem) {
        localDataSource.deleteItem(todoItem)
    }

    fun getNextId(): String{
        return "${_dataStateFlow.value.size}"
    }
}