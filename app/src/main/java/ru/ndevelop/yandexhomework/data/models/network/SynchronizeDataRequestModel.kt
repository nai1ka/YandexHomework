package ru.ndevelop.yandexhomework.data.models.network

import kotlinx.serialization.Serializable

@Serializable
data class SynchronizeDataRequestModel(
    val list: List<ItemResponseModel>
)