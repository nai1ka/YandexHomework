package ru.ndevelop.yandexhomework.presentation.viewmodels

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.ndevelop.yandexhomework.App
import ru.ndevelop.yandexhomework.core.models.TodoItem
import ru.ndevelop.yandexhomework.core.models.TodoItemImportance
import ru.ndevelop.yandexhomework.data.TodoItemsRepository
import ru.ndevelop.yandexhomework.presentation.AddItemUiEffect
import ru.ndevelop.yandexhomework.presentation.screens.addItem.AddItemUiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
class AddItemViewModel(
    private val todoItemsRepository: TodoItemsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var todoItem: TodoItem? = null

    private val _uiState = MutableStateFlow(AddItemUiState())
    val uiState: StateFlow<AddItemUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AddItemUiEffect>(replay = 0, extraBufferCapacity = 1)
    val uiEffect: SharedFlow<AddItemUiEffect> = _uiEffect

    fun onTextChanged(text: String) {
        _uiState.update { _uiState.value.copy(text = text) }
    }

    fun onImportanceChanged(importance: TodoItemImportance) {
        _uiState.update { _uiState.value.copy(importance = importance) }
    }

    fun saveItem() {
        if (todoItem == null) {
            val currentTime = System.currentTimeMillis()
            val item = TodoItem(
                id = currentTime.toString(),
                text = _uiState.value.text,
                importance = _uiState.value.importance,
                deadline = if (_uiState.value.isDatePickerExpanded) {
                    _uiState.value.datePickerState.selectedDateMillis
                } else {
                    null
                },
                isCompleted = false,
                creationDate = currentTime
            )
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    todoItemsRepository.addItem(item)
                    _uiEffect.emit(AddItemUiEffect.GoBack)
                } catch (e: Exception) {
                    if (e !is CancellationException) _uiEffect.tryEmit(AddItemUiEffect.ShowError("Failed to save item"))
                }
            }

        } else {
            val item = todoItem!!.copy(
                text = _uiState.value.text,
                importance = _uiState.value.importance,
                deadline = if (_uiState.value.isDatePickerExpanded) {
                    _uiState.value.datePickerState.selectedDateMillis
                } else {
                    null
                },
                updateDate = System.currentTimeMillis()
            )
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    todoItemsRepository.updateItem(item)
                    _uiEffect.emit(AddItemUiEffect.GoBack)
                } catch (e: Exception) {
                    if (e !is CancellationException) _uiEffect.tryEmit(AddItemUiEffect.ShowError("Failed to update item"))
                }
            }
        }
    }

    fun setItem(todoItem: TodoItem) {
        this.todoItem = todoItem
        _uiState.update {
            _uiState.value.copy(
                text = todoItem.text,
                importance = todoItem.importance,
                datePickerState = DatePickerState(
                    initialSelectedDateMillis = todoItem.deadline,
                    locale = Locale.getDefault(),
                    yearRange = _uiState.value.datePickerState.yearRange,
                    selectableDates = _uiState.value.datePickerState.selectableDates
                ),
                isDatePickerExpanded = todoItem.deadline != null,
            )
        }
    }

    fun deleteItem() {
        viewModelScope.launch(Dispatchers.IO) {
            todoItem?.let {
                try {
                    todoItemsRepository.deleteItem(it)
                    _uiEffect.emit(AddItemUiEffect.GoBack)
                } catch (e: Exception) {
                    if (e !is CancellationException) _uiEffect.tryEmit(AddItemUiEffect.ShowError("Failed to delete item"))
                }
            }
        }

    }

    fun setIsDatePickerExpanded(isExpanded: Boolean) {
        _uiState.update { _uiState.value.copy(isDatePickerExpanded = isExpanded) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val myRepository = (this[APPLICATION_KEY] as App).todoItemsRepository
                AddItemViewModel(
                    todoItemsRepository = myRepository, savedStateHandle = savedStateHandle
                )
            }
        }
    }
}