package ru.ndevelop.yandexhomework.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemResponseModel(
    @SerialName("last_updated_by")
    val lastUpdatedBy: String,
    val deadline: Long? = null,
    @SerialName("changed_at")
    val changedAt: Long,
    @SerialName("created_at")
    val createdAt: Long,
    val id: String,
    val done: Boolean,
    val text: String,
    val color: String?,
    val importance: String
)