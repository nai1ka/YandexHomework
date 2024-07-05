package ru.ndevelop.yandexhomework.presentation.composables

import DeadlineSelector
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import ru.ndevelop.yandexhomework.presentation.viewmodels.AddItemViewModel

@ExperimentalMaterial3Api
@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel,
    navController: NavController,
    isDeleteButtonEnabled: Boolean = false,
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            AppBar(onExitClick = {
                navController.navigateUp()
            }, onSaveClick = {
                viewModel.saveItem()
                //navController.navigateUp()
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
            }
        }
    }
}