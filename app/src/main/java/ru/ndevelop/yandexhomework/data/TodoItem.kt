package ru.ndevelop.yandexhomework.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    val id: String,
    val text: String,
    val importance: TodoItemImportance,
    val deadline: Long? = null,
    val isCompleted: Boolean,
    val creationDate: Long,
    val updateDate: Long? = null
) : Parcelable


enum class TodoItemImportance {
    LOW, NORMAL, HIGH
}