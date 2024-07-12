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
import javax.inject.Inject


class TodoItemsRepository constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val deviceID: String
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
        localDataSource.getFlow().collect { data ->
            _dataStateFlow.emit(data)
        }
    }

    suspend fun addItem(item: TodoItem) {
        localDataSource.addItem(item)
        remoteDataSource.addItem(item,RetrofitClient.knownRevision,deviceID)
    }

    suspend fun synchronizeData() {
        val localData = localDataSource.getItems()
        remoteDataSource.synchronizeData(localData, RetrofitClient.knownRevision,deviceID)
    }

    suspend fun fetchFromNetwork(){
        _dataStateFlow.emit(remoteDataSource.getListOfItems())
    }

    suspend fun deleteItem(todoItem: TodoItem) {
        localDataSource.deleteItem(todoItem)
        remoteDataSource.deleteItem(todoItem.id,RetrofitClient.knownRevision)
    }

    suspend fun updateItem(todoItem: TodoItem) {
        localDataSource.updateItem(todoItem)
        remoteDataSource.updateItem(todoItem, RetrofitClient.knownRevision,deviceID)
    }

}