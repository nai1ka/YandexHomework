package ru.ndevelop.yandexhomework.core.listeners

interface OnItemClickListener {
    fun onItemClick(index: Int)
    fun onItemCheckedChange(index: Int, isChecked: Boolean)
    fun onNewItemClick()
}