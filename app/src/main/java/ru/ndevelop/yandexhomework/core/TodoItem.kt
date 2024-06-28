package ru.ndevelop.yandexhomework.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    val id: String,
    val text: String,
    val importance: TodoItemImportance,
    val isCompleted: Boolean,
    val creationDate: Long,
    val deadline: Long? = null,
    val updateDate: Long? = null
) : Parcelable


enum class TodoItemImportance(val title: String) {
    LOW("Низкий"), NORMAL("Нет"), HIGH("!! Высокий")
}