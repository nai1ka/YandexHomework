package ru.ndevelop.yandexhomework.data.models.network

import kotlinx.serialization.Serializable

@Serializable
data class ListOfItemsResponseModel(
    val revision: Int,
    val status: String,
    val list: List<ItemResponseModel>
)