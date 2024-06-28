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
import ru.ndevelop.yandexhomework.core.TodoItem
import ru.ndevelop.yandexhomework.data.TodoItemsRepository
import ru.ndevelop.yandexhomework.presentation.LceState
import ru.ndevelop.yandexhomework.presentation.UiEffect
import ru.ndevelop.yandexhomework.presentation.screens.itemList.ItemListUiState

class ItemListViewModel(
    private val repository: TodoItemsRepository,
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
    private val _uiState = MutableStateFlow<LceState<ItemListUiState>>(LceState.Loading)
    val uiState: StateFlow<LceState<ItemListUiState>> = _uiState

    private val _uiEffect = MutableSharedFlow<UiEffect>(replay = 0, extraBufferCapacity = 1)
    val uiEffect: SharedFlow<UiEffect> = _uiEffect

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.loadLocalData()
            } catch (e: Exception) {
                if (e !is CancellationException) _uiEffect.tryEmit(UiEffect.ShowError("Failed to load list of tasks"))
            }
            repository.dataStateFlow.collect {
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
                    repository = myRepository, savedStateHandle = savedStateHandle
                )
            }
        }
    }

    fun deleteItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteItem(item)
            } catch (e: Exception) {
                if (e !is CancellationException) _uiEffect.tryEmit(UiEffect.ShowError("Failed to delete item"))
            }
        }
    }

    fun changeCompletedState(item: TodoItem, isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateItem(item.copy(isCompleted = isCompleted))
            } catch (e: Exception) {
                if (e !is CancellationException) _uiEffect.tryEmit(UiEffect.ShowError("Failed to update item"))
            }
        }
    }
}