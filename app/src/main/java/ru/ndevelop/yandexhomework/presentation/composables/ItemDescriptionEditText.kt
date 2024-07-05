package ru.ndevelop.yandexhomework.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ndevelop.yandexhomework.R

@Composable
fun ItemDescriptionEditText(onItemNameChanged: (String) -> Unit, currentItemName: String) {
    OutlinedTextField(
        value = currentItemName,
        onValueChange = onItemNameChanged,
        placeholder = { Text(text = stringResource(R.string.what_left_to_do)) },
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
            .background(MaterialTheme.colorScheme.surface)
    )
}