package ru.ndevelop.yandexhomework.presentation.screens.addItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.core.TodoItemImportance
import ru.ndevelop.yandexhomework.core.toStringDate
import ru.ndevelop.yandexhomework.data.LocalDataSourceImpl
import ru.ndevelop.yandexhomework.data.RemoteDataSourceImpl
import ru.ndevelop.yandexhomework.data.TodoItemsRepository
import ru.ndevelop.yandexhomework.presentation.UiEffect
import ru.ndevelop.yandexhomework.presentation.theme.AppTheme
import ru.ndevelop.yandexhomework.presentation.theme.Colors
import ru.ndevelop.yandexhomework.presentation.viewmodels.AddItemViewModel

class AddItemFragment : Fragment() {
    private val viewModel: AddItemViewModel by viewModels { AddItemViewModel.Factory }
    private val args: AddItemFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val todoItem = args.todoItem
        if (savedInstanceState == null && todoItem != null) {
            viewModel.setItem(todoItem)
        }

    }


    @ExperimentalMaterial3Api
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        subscribeToViewModel()
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    AddItemScreen(
                        viewModel = viewModel,
                        navController = findNavController(),
                        isDeleteButtonEnabled = args.todoItem != null
                    )
                }
            }
        }
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEffect.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { effect ->
                handleEffect(effect)
            }
        }
    }

    private fun handleEffect(effect: UiEffect) {
        when (effect) {
            is UiEffect.ShowError -> {
                Toast.makeText(requireContext(), effect.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel,
    navController: NavController,
    isDeleteButtonEnabled: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(

        topBar = {
            AppBar(onExitClick = {
                navController.navigateUp()
            }, onSaveClick = {
                viewModel.saveItem()
                navController.navigateUp()
            })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                ItemDescriptionEditText(
                    onItemNameChanged = { viewModel.onTextChanged(it) },
                    currentItemName = uiState.text
                )
                ImportanceSelector(selectedImportance = uiState.importance,
                    onImportanceSelected = {
                        viewModel.onImportanceChanged(it)
                    })
                HorizontalDivider()


                DeadlineSelector(
                    uiState.datePickerState,
                    isExpanded = uiState.isDatePickerExpanded
                ) {
                    viewModel.setIsDatePickerExpanded(it)
                }
            }
            HorizontalDivider()
            DeleteButton(isEnabled = isDeleteButtonEnabled) {
                viewModel.deleteItem()
                navController.navigateUp()
            }
        }
    }
}


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
                text = "СОХРАНИТЬ", modifier = Modifier.padding(1.dp)
            )
        }
    }
}

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

@Composable
fun ImportanceSelector(
    onImportanceSelected: (TodoItemImportance) -> Unit, selectedImportance: TodoItemImportance
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.clickable {
        expanded = true
    }) {
        Text(text = "Важность", style = MaterialTheme.typography.bodyMedium)
        Text(text = selectedImportance.title, color = Color.Gray)
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(text = TodoItemImportance.LOW.title) }, onClick = {
                onImportanceSelected(TodoItemImportance.LOW)
                expanded = false
            })
            DropdownMenuItem(text = { Text(text = TodoItemImportance.NORMAL.title) }, onClick = {
                onImportanceSelected(TodoItemImportance.NORMAL)
                expanded = false
            })
            DropdownMenuItem(text = {
                Text(
                    text = TodoItemImportance.HIGH.title,
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
                Text(text = "Сделать до", style = MaterialTheme.typography.bodyMedium)
                if (isExpanded) {
                    Text(
                        text = datePickerState.selectedDateMillis?.toStringDate("d MMMM") ?: "",
                        color = MaterialTheme.colorScheme.primary
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


@Composable
fun ThemedPreview(darkTheme: Boolean, content: @Composable () -> Unit) {
    AppTheme(useDarkTheme = darkTheme) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Light Theme")
@Composable
fun AddItemScreenLightPreview() {
    ThemedPreview(darkTheme = false) {
        AddItemScreen(
            viewModel = AddItemViewModel(
                TodoItemsRepository(
                    RemoteDataSourceImpl(),
                    LocalDataSourceImpl()
                ), SavedStateHandle()
            ),
            navController = NavController(LocalContext.current)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun AddItemScreenDarkPreview() {
    ThemedPreview(darkTheme = true) {
        AddItemScreen(
            viewModel = AddItemViewModel(
                TodoItemsRepository(
                    RemoteDataSourceImpl(),
                    LocalDataSourceImpl()
                ), SavedStateHandle()
            ),
            navController = NavController(LocalContext.current)
        )
    }
}