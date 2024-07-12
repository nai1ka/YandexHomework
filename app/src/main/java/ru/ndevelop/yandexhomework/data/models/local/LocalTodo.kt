package ru.ndevelop.yandexhomework.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.ndevelop.yandexhomework.core.models.TodoItem
import ru.ndevelop.yandexhomework.core.models.TodoItemImportance

@Entity(tableName = "todos")
data class LocalTodo(
    @PrimaryKey val id: String,
    val text: String,
    // TODO maybe custom type
    val importance: String,
    val isCompleted: Boolean,
    val creationDate: Long,
    val deadline: Long?,
    val updateDate: Long?
)


fun LocalTodo.toDomainModel(): TodoItem = TodoItem(
    id = id,
    text = text,
    importance = TodoItemImportance.valueOf(importance),
    isCompleted = isCompleted,
    creationDate = creationDate,
    deadline = deadline,
    updateDate = updateDate
)

fun TodoItem.toLocalModel(): LocalTodo = LocalTodo(
    id = id,
    text = text,
    importance = importance.name,
    isCompleted = isCompleted,
    creationDate = creationDate,
    deadline = deadline,
    updateDate = updateDate
)