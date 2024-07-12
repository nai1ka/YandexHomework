package ru.ndevelop.yandexhomework.presentation


sealed interface AddItemUiEffect {
    data class ShowError(val errorMessage: String) : AddItemUiEffect
    data object GoBack: AddItemUiEffect
}