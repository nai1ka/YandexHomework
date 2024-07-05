package ru.ndevelop.yandexhomework.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.core.models.TodoItemImportance

@Composable
fun ImportanceSelector(
    onImportanceSelected: (TodoItemImportance) -> Unit, selectedImportance: TodoItemImportance
) {
    var expanded by remember { mutableStateOf(false) }
    val title = when (selectedImportance) {
        TodoItemImportance.LOW -> stringResource(id = R.string.low_importance)
        TodoItemImportance.NORMAL -> stringResource(id = R.string.normal_importance)
        TodoItemImportance.HIGH -> stringResource(id = R.string.high_importance)
    }
    Column(modifier = Modifier.clickable {
        expanded = true
    }) {
        Text(
            text = stringResource(id = R.string.importance),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(text = title, color = Color.Gray)
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.low_importance)) },
                onClick = {
                    onImportanceSelected(TodoItemImportance.LOW)
                    expanded = false
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.normal_importance)) },
                onClick = {
                    onImportanceSelected(TodoItemImportance.NORMAL)
                    expanded = false
                })
            DropdownMenuItem(text = {
                Text(
                    text = stringResource(id = R.string.high_importance),
                    color = Color.Red
                )
            },
                onClick = {
                    onImportanceSelected(TodoItemImportance.HIGH)
                    expanded = false
                })
        }
    }
}
