package ru.ndevelop.yandexhomework.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toStringDate(pattern: String = "d MMMM yyyy"): String {
    val locale = Locale("ru")
    val date = Date(this)
    val format = SimpleDateFormat(pattern, locale)
    return format.format(date)
}