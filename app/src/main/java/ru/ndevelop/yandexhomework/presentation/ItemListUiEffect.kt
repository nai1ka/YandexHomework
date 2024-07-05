package ru.ndevelop.yandexhomework.presentation


sealed interface ItemListUiEffect {
    data class ShowError(val errorMessage: String, val itemPosition: Int? = null) : ItemListUiEffect
}