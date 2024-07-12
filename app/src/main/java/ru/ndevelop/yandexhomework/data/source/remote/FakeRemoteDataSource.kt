package ru.ndevelop.yandexhomework.data.source.remote

import ru.ndevelop.yandexhomework.core.models.TodoItem

class FakeRemoteDataSource : RemoteDataSource {

    override suspend fun getListOfItems(): List<TodoItem> {
        return emptyList()
    }

    override suspend fun deleteItem(id: String, revision: Int) {}

    override suspend fun addItem(item: TodoItem, revision: Int, deviceID: String) {}

    override suspend fun updateItem(item: TodoItem, revision: Int, deviceID: String) {}

    override suspend fun synchronizeData(items: List<TodoItem>, revision: Int, deviceID: String) {}


}

