package ru.ndevelop.yandexhomework.presentation.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.presentation.theme.Colors

@Composable
fun DeleteButton(isEnabled: Boolean, onClick: () -> Unit) {
    TextButton(onClick = onClick, enabled = isEnabled) {
        Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            tint = if (isEnabled) Colors.Day.red else MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            style = MaterialTheme.typography.titleSmall,
            text = stringResource(id = R.string.delete),
            color = if (isEnabled) Colors.Day.red else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}