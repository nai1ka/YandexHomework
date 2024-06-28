package ru.ndevelop.yandexhomework.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ndevelop.yandexhomework.core.TodoItem

class ItemsDiff : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}