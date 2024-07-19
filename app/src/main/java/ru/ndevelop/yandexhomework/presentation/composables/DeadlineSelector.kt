import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.core.toStringDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlineSelector(
    datePickerState: DatePickerState,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit
) {
    Column {
        Row {
            Column {
                Text(text = stringResource(id = R.string.do_until), style = MaterialTheme.typography.bodyMedium)
                if (isExpanded) {
                    Text(
                        text = datePickerState.selectedDateMillis?.toStringDate("d MMMM") ?: "",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isExpanded, onCheckedChange = onExpandChange)
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {

            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxWidth(),
                headline = null,
                title = null,
                showModeToggle = false,
            )
        }


    }
}