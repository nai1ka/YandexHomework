package ru.ndevelop.yandexhomework.core

import androidx.recyclerview.widget.DiffUtil

import ru.ndevelop.yandexhomework.data.TodoItem

class ItemsDiff : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}