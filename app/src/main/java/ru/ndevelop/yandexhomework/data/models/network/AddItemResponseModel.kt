package ru.ndevelop.yandexhomework.data.models.network

import kotlinx.serialization.Serializable

@Serializable
data class AddItemResponseModel(
    val revision: Int,
    val status: String
)