package ru.ndevelop.yandexhomework.presentation

sealed interface LceState<out R> {
    data class Content<out T>(val content: T) : LceState<T>
    data object Loading : LceState<Nothing>
    data class Error(val throwable: Throwable) : LceState<Nothing>

    fun hasData(): Boolean{
        return this is Content
    }
    fun requireData(): R {
        return (this as Content<R>).content
    }
}




