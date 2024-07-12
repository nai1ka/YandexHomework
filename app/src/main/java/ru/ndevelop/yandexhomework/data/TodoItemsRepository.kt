package ru.ndevelop.yandexhomework.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.ndevelop.yandexhomework.core.models.TodoItem
import ru.ndevelop.yandexhomework.core.api.RetrofitClient
import ru.ndevelop.yandexhomework.data.source.local.LocalDataSource
import ru.ndevelop.yandexhomework.data.source.remote.RemoteDataSource


class TodoItemsRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    private val _dataStateFlow = MutableSharedFlow<List<TodoItem>>(1)
    val dataStateFlow: SharedFlow<List<TodoItem>>
        get() = _dataStateFlow

    init {
        CoroutineScope(Dispatchers.IO).launch {
            subscribeToUpdates()
        }
    }
    private suspend fun subscribeToUpdates() {
        localDataSource.getData().collect { data ->
            _dataStateFlow.emit(data)
        }
    }

    suspend fun addItem(item: TodoItem) {
        remoteDataSource.addItem(item,RetrofitClient.knownRevision)
    }

    suspend fun fetchData() {
        val data = remoteDataSource.getListOfItems()
        _dataStateFlow.emit(data)
    }

    suspend fun deleteItem(todoItem: TodoItem) {
        remoteDataSource.deleteItem(todoItem.id,RetrofitClient.knownRevision)
    }

    suspend fun updateItem(todoItem: TodoItem) {
        remoteDataSource.updateItem(todoItem, RetrofitClient.knownRevision)
    }

}