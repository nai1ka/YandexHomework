package ru.ndevelop.yandexhomework.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.ndevelop.yandexhomework.data.TodoItem
import ru.ndevelop.yandexhomework.data.TodoItemsRepository

class ItemListViewModel : ViewModel() {
    var numberOfCompletedItems: Int = 0
    private val toDoItemRepository = TodoItemsRepository
    private val _dataState = MutableStateFlow<List<TodoItem>>(emptyList())

    var doneItemsVisible = false
        set(value) {
            field = value
            fetchData()
        }

    val dataState: StateFlow<List<TodoItem>> = _dataState

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            val allItems = toDoItemRepository.getLisOfItems()
            val resultItems = if (!doneItemsVisible) allItems.filter { !it.isCompleted } else allItems

            numberOfCompletedItems = allItems.count { it.isCompleted }
            _dataState.value = resultItems

        }
    }

    fun removeItem(todoItem: TodoItem) {
        viewModelScope.launch {
            toDoItemRepository.removeItem(todoItem)
            fetchData()
        }
    }

    fun updateItem(todoItem: TodoItem) {
        viewModelScope.launch {
            toDoItemRepository.updateItem(todoItem)
            fetchData()
        }
    }
    fun addItem(todoItem: TodoItem) {
        viewModelScope.launch {
            toDoItemRepository.addNewItem(todoItem)
            fetchData()
        }
    }

    fun changeCompletedState(item: TodoItem, completed: Boolean) {
        viewModelScope.launch {
            toDoItemRepository.updateItem(item.copy(isCompleted = completed))
            fetchData()
        }
    }
}