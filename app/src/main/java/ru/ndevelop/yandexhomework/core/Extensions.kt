package ru.ndevelop.yandexhomework.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toStringDate(): String {
    val locale = Locale("ru")
    val date = Date(this)
    val format = SimpleDateFormat("d MMMM yyyy", locale)
    return format.format(date)
}