package ru.ndevelop.yandexhomework.data.source.remote

import ru.ndevelop.tinkoffproject.core.api.TodoApi
import ru.ndevelop.yandexhomework.core.api.RetrofitClient
import ru.ndevelop.yandexhomework.core.models.TodoItem
import ru.ndevelop.yandexhomework.core.parseList
import ru.ndevelop.yandexhomework.core.toNetworkModel
import ru.ndevelop.yandexhomework.data.models.network.AddItemRequestModel
import ru.ndevelop.yandexhomework.data.models.network.SynchronizeDataRequestModel
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val todoApi: TodoApi,
) : RemoteDataSource {

    override suspend fun getListOfItems(): List<TodoItem> {
        val rawResult = todoApi.getListOfItems()
        RetrofitClient.knownRevision = rawResult.revision
        return rawResult.parseList()
    }

    override suspend fun deleteItem(id: String, revision: Int) {
        val rawResult = todoApi.deleteItem(id, revision)
        RetrofitClient.knownRevision = rawResult.revision
    }

    override suspend fun addItem(item: TodoItem, revision: Int, deviceID: String) {
        val rawResult =
            todoApi.addItem(AddItemRequestModel(item.toNetworkModel(deviceID)), revision)
        RetrofitClient.knownRevision = rawResult.revision
    }

    override suspend fun updateItem(item: TodoItem, revision: Int, deviceID: String) {
        val rawResult =
            todoApi.updateItem(
                item.id,
                AddItemRequestModel(item.toNetworkModel(deviceID)),
                revision
            )
        RetrofitClient.knownRevision = rawResult.revision
    }

    override suspend fun synchronizeData(items: List<TodoItem>, revision: Int, deviceID: String) {
        val rawResult = todoApi.synchronizeData(
            SynchronizeDataRequestModel(items.map { it.toNetworkModel(deviceID) }),
            revision
        )
        RetrofitClient.knownRevision = rawResult.revision
    }
}

