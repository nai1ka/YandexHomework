package ru.ndevelop.yandexhomework.data

import java.util.Date


object TodoItemsRepository {
    private var items = arrayListOf(
        TodoItem(
            "1", "Купить что-то", TodoItemImportance.LOW, null, false, 0, null
        ), TodoItem(
            "2",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
            TodoItemImportance.NORMAL,
            null,
            true,
            0,
            null
        ), TodoItem(
            "3",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обращаться",
            TodoItemImportance.NORMAL,
            null,
            false,
            0,
            null
        ), TodoItem(
            "4",
            "Купить что-то",
            TodoItemImportance.HIGH,
            null,
            false,
            0,
            null

        ), TodoItem(
            "5",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
            TodoItemImportance.LOW,
            null,
            true,
            0,
            Date().time
        ), TodoItem(
            "6",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus consequat aliquam ex, non molestie ligula viverra quis. Quisque in vestibulum nibh, ultrices condimentum eros.",
            TodoItemImportance.NORMAL,
            Date().time + 10000,
            false,
            0,
            null
        ), TodoItem(
            "7", "Купить что-то", TodoItemImportance.NORMAL, null, false, 0, null
        ), TodoItem(
            "8", "Купить что-то", TodoItemImportance.HIGH, null, false, 0, null
        ), TodoItem(
            "9", "Купить что-то", TodoItemImportance.LOW, null, false, 0, null
        ), TodoItem(
            "10", "Купить что-то", TodoItemImportance.NORMAL, null, false, 0, null
        ),
        TodoItem(
            "11", "123", TodoItemImportance.HIGH, 123, true, 123123, 45345534
        ),
        TodoItem(
            "12", "1231231231", TodoItemImportance.LOW, 123123, false, 123123, 45312312345534
        ),
        TodoItem(
            "13", "1231231231", TodoItemImportance.HIGH, 123123, false, 123123, 45312312345534
        ),
        TodoItem(
            "14", "1231231231", TodoItemImportance.NORMAL, 123123, false, 123123, 45312312345534
        ),
        TodoItem(
            "15", "1231231231", TodoItemImportance.HIGH, 123123, false, 123123, 45312312345534
        ),
        TodoItem(
            "16", "1231231231", TodoItemImportance.LOW, 123123, false, 123123, 45312312345534
        )
    )

    fun getLisOfItems(): List<TodoItem> = items.toMutableList()

    fun removeItem(todoItem: TodoItem) {
        items.removeIf { it.id == todoItem.id }
    }

    fun addNewItem(todoItem: TodoItem) {
        items.add(todoItem)
    }

    fun updateItem(todoItem: TodoItem) {
        items.replaceAll {
            if (it.id == todoItem.id) todoItem else it
        }
    }
}