package ru.ndevelop.yandexhomework.presentation.screens.addItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import ru.ndevelop.yandexhomework.appComponent
import ru.ndevelop.yandexhomework.data.TodoItemsRepository
import ru.ndevelop.yandexhomework.data.source.local.FakeLocalDataSource
import ru.ndevelop.yandexhomework.data.source.remote.FakeRemoteDataSource
import ru.ndevelop.yandexhomework.presentation.AddItemUiEffect
import ru.ndevelop.yandexhomework.presentation.composables.AddItemScreen
import ru.ndevelop.yandexhomework.presentation.theme.AppTheme
import ru.ndevelop.yandexhomework.presentation.viewmodels.AddItemViewModel
import ru.ndevelop.yandexhomework.presentation.viewmodels.Factory

class AddItemFragment : Fragment() {
    private val viewModel: AddItemViewModel by viewModels {
        Factory {
            requireContext().appComponent().addItemViewModel
        }
    }
    private val args: AddItemFragmentArgs by navArgs()
    private val snackbarHostState = SnackbarHostState()
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
                        isDeleteButtonEnabled = args.todoItem != null,
                        snackbarHostState = snackbarHostState
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

    private fun handleEffect(effect: AddItemUiEffect) {
        when (effect) {
            is AddItemUiEffect.ShowError -> {
                lifecycleScope.launch {
                    snackbarHostState.showSnackbar(effect.errorMessage)
                }
            }

            AddItemUiEffect.GoBack -> {
                setFragmentResult(REQUEST_KEY, bundleOf("refresh" to true))
                findNavController().navigateUp()
            }
        }
    }

    companion object {
        const val REQUEST_KEY: String = "add_item"
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
                TodoItemsRepository(FakeRemoteDataSource(), FakeLocalDataSource())
            ),
            navController = NavController(LocalContext.current),
            snackbarHostState = SnackbarHostState()
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
                TodoItemsRepository(FakeRemoteDataSource(), FakeLocalDataSource())
            ),
            navController = NavController(LocalContext.current),
            snackbarHostState = SnackbarHostState()
        )
    }
}