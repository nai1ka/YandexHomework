package ru.ndevelop.yandexhomework.presentation.screens.itemList

import ru.ndevelop.yandexhomework.core.TodoItem


data class ItemListUiState(
    val items: List<TodoItem>,
    val numberOfCompletedItems: Int
)