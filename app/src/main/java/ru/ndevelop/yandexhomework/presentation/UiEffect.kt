package ru.ndevelop.yandexhomework.presentation


sealed interface UiEffect {
    data class ShowError(val errorMessage: String) : UiEffect
}