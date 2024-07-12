package ru.ndevelop.yandexhomework.data.source.remote

import ru.ndevelop.tinkoffproject.core.api.TodoApi
import ru.ndevelop.yandexhomework.core.api.RetrofitClient
import ru.ndevelop.yandexhomework.core.models.TodoItem
import ru.ndevelop.yandexhomework.core.parseList
import ru.ndevelop.yandexhomework.core.toNetworkModel
import ru.ndevelop.yandexhomework.data.models.network.AddItemRequestModel
import javax.inject.Inject

class FakeRemoteDataSource: RemoteDataSource {

    override suspend fun getListOfItems(): List<TodoItem> {
        return emptyList()
    }
    override suspend fun deleteItem(id: String, revision: Int) {}

    override suspend fun addItem(item: TodoItem, revision: Int) {}

    override suspend fun updateItem(item: TodoItem, revision: Int) {}

    override suspend fun synchronizeData(items: List<TodoItem>, revision: Int) {}


}

