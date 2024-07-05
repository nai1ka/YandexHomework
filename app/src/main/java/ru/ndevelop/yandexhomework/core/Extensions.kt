package ru.ndevelop.yandexhomework.core

import ru.ndevelop.yandexhomework.core.models.TodoItem
import ru.ndevelop.yandexhomework.core.models.TodoItemImportance
import ru.ndevelop.yandexhomework.data.models.network.ItemResponseModel
import ru.ndevelop.yandexhomework.data.models.network.ListOfItemsResponseModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toStringDate(pattern: String = "d MMMM yyyy"): String {
    val locale = Locale("ru")
    val date = Date(this)
    val format = SimpleDateFormat(pattern, locale)
    return format.format(date)
}
fun ItemResponseModel.parseModel(): TodoItem {
    return TodoItem(
        id = id,
        text = text,
        importance = parseImportance(importance),
        isCompleted = done,
        creationDate = createdAt,
        deadline = if (deadline == 0L) null else deadline,
        updateDate = if (changedAt == 0L) null else changedAt
    )
}

fun parseImportance(importance: String) = when (importance) {
    "low" -> TodoItemImportance.LOW
    "basic" -> TodoItemImportance.NORMAL
    "important" -> TodoItemImportance.HIGH
    else -> TodoItemImportance.NORMAL
}


fun TodoItem.toNetworkModel(deviceID: String): ItemResponseModel {
    return ItemResponseModel(
        id = id,
        text = text,
        done = isCompleted,
        createdAt = creationDate,
        deadline = deadline ?: 0,
        changedAt = updateDate ?: 0,
        lastUpdatedBy = deviceID,
        color = "white",
        importance = importance.code
    )
}


fun ListOfItemsResponseModel.parseList(): List<TodoItem> {
    return list.map { it.parseModel() }
}