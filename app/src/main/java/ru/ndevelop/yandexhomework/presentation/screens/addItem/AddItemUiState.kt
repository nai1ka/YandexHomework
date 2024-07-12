package ru.ndevelop.yandexhomework.presentation.screens.addItem

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import ru.ndevelop.yandexhomework.core.models.TodoItemImportance
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
data class AddItemUiState(
    val text: String = "",
    val importance: TodoItemImportance = TodoItemImportance.NORMAL,
    val datePickerState: DatePickerState = DatePickerState(
        locale = Locale.getDefault(),
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = FutureOrPresentSelectableDates
    ),
    val isDatePickerExpanded: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
object FutureOrPresentSelectableDates: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val todayStartUtc = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return utcTimeMillis >= todayStartUtc
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}