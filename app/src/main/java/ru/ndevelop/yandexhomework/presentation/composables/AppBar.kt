package ru.ndevelop.yandexhomework.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.ndevelop.yandexhomework.R

@Composable
fun AppBar(onExitClick: () -> Unit, onSaveClick: () -> Unit) {
    Row {
        IconButton(onClick = onExitClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close), contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onSaveClick) {
            Text(
                text = stringResource(id = R.string.save),
                modifier = Modifier.padding(1.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}