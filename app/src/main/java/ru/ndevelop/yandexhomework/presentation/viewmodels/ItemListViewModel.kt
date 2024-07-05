package ru.ndevelop.yandexhomework.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.ndevelop.yandexhomework.App
import ru.ndevelop.yandexhomework.core.models.TodoItem
import ru.ndevelop.yandexhomework.data.TodoItemsRepository
import ru.ndevelop.yandexhomework.presentation.ItemListUiEffect
import ru.ndevelop.yandexhomework.presentation.LceState
import ru.ndevelop.yandexhomework.presentation.screens.itemList.ItemListUiState

class ItemListViewModel(
    private val todoItemsRepository: TodoItemsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var areCompletedItemsVisible: Boolean = false
        set(value) {
            field = value
            if (_uiState.value.hasData()) {
                _uiState.update { state ->
                    LceState.Content(
                        state.requireData().copy(
                            items = itemList.filter { it.isCompleted == value || areCompletedItemsVisible })
                    )
                }
            }
        }

    private var itemList: List<TodoItem> = emptyList()
        set(value) {
            field = value
            if (_uiState.value.hasData()) {
                val numberOfCompletedItems = field.count { item -> item.isCompleted }
                _uiState.update { state ->
                    LceState.Content(
                        state.requireData()
                            .copy(
                                items = itemList.filter { it.isCompleted == areCompletedItemsVisible || areCompletedItemsVisible },
                                numberOfCompletedItems = numberOfCompletedItems
                            )
                    )
                }
            }
        }
    private val _uiState = MutableStateFlow<LceState<ItemListUiState>>(LceState.Loading)
    val uiState: StateFlow<LceState<ItemListUiState>> = _uiState

    private val _uiEffect = MutableSharedFlow<ItemListUiEffect>(replay = 0, extraBufferCapacity = 1)
    val uiEffect: SharedFlow<ItemListUiEffect> = _uiEffect

    init {
        fetchItems()
        viewModelScope.launch(Dispatchers.IO) {
            todoItemsRepository.dataStateFlow.collect {
                val numberOfCompletedItems = it.count { item -> item.isCompleted }
                itemList = it
                _uiState.value = LceState.Content(
                    ItemListUiState(
                        items = itemList.filter { item -> item.isCompleted == areCompletedItemsVisible || areCompletedItemsVisible },
                        numberOfCompletedItems = numberOfCompletedItems
                    )
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val myRepository = (this[APPLICATION_KEY] as App).todoItemsRepository
                ItemListViewModel(
                    todoItemsRepository = myRepository, savedStateHandle = savedStateHandle
                )
            }
        }
    }

    fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(LceState.Loading)
            try {
                todoItemsRepository.fetchData()
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    _uiState.emit(LceState.Error)
                    _uiEffect.tryEmit(ItemListUiEffect.ShowError("Failed to load list of tasks"))
                }
            }
        }
    }

    fun deleteItem(item: TodoItem, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                todoItemsRepository.deleteItem(item)
                todoItemsRepository.fetchData()
            } catch (e: Exception) {
                if (e !is CancellationException) _uiEffect.tryEmit(
                    ItemListUiEffect.ShowError(
                        "Failed to delete item",
                        position
                    )
                )
            }
        }
    }

    fun changeCompletedState(item: TodoItem, isCompleted: Boolean, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val indexOfItem = itemList.indexOfFirst { it.id == item.id }
            if (indexOfItem != -1) {
                val updatedItems = itemList.toMutableList()
                try {
                    todoItemsRepository.updateItem(item.copy(isCompleted = isCompleted))
                    updatedItems[indexOfItem] = item.copy(isCompleted = isCompleted)
                    itemList = updatedItems
                } catch (e: Exception) {
                    if (e !is CancellationException) _uiEffect.tryEmit(
                        ItemListUiEffect.ShowError(
                            "Failed to update item",
                            position
                        )
                    )
                }
            }
        }
    }
}