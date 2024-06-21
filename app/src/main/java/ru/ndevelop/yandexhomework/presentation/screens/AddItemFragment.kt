package ru.ndevelop.yandexhomework.presentation.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.data.TodoItem
import ru.ndevelop.yandexhomework.data.TodoItemImportance
import ru.ndevelop.yandexhomework.data.TodoItemsRepository
import ru.ndevelop.yandexhomework.databinding.FragmentAddItemBinding
import ru.ndevelop.yandexhomework.presentation.viewmodels.ItemListViewModel
import ru.ndevelop.yandexhomework.core.toStringDate
import java.util.Calendar
import java.util.Date

class AddItemFragment : Fragment(R.layout.fragment_add_item) {
    private lateinit var binding: FragmentAddItemBinding
    private val viewModel: ItemListViewModel by activityViewModels()

    private val args: AddItemFragmentArgs by navArgs()
    private val todoItem: TodoItem? by lazy { args.todoItem }
    private var selectedDeadline: Long = Date().time


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupItem(todoItem)
    }

    private fun setupItem(todoItem: TodoItem?) {
        if (todoItem == null) return
        binding.importanceSpinner.setSelection(todoItem.importance.ordinal)
        binding.editText.setText(todoItem.text)
        binding.deleteButton.enable()
        if (todoItem.deadline == null) {
            binding.deadlineSwitch.isChecked = false
            binding.selectedDeadline.visibility = View.INVISIBLE
        } else {
            binding.deadlineSwitch.isChecked = true
            selectedDeadline = todoItem.deadline
            binding.selectedDeadline.text = selectedDeadline!!.toStringDate()
        }
    }

    private fun setupUI() {
        binding.deadlineSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.selectedDeadline.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
        }
        binding.exitButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.saveButton.setOnClickListener {
            findNavController().navigateUp()
            saveItem()
        }
        binding.deleteButton.setOnClickListener {
            todoItem?.let {
                viewModel.removeItem(it)
                findNavController().navigateUp()
            }
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.importance_array,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.importanceSpinner.adapter = adapter
        }
        binding.importanceSpinner.setSelection(TodoItemImportance.NORMAL.ordinal)
        binding.selectedDeadline.text = Date().time.toStringDate()
        binding.selectedDeadline.setOnClickListener {
            showDatepickerDialog()
        }
    }

    private fun saveItem() {
        val text = binding.editText.text.toString()
        val importance = TodoItemImportance.entries[binding.importanceSpinner.selectedItemPosition]
        val deadline: Long? = if (binding.deadlineSwitch.isChecked) {
            selectedDeadline
        } else {
            null
        }
        if (todoItem != null) {
            viewModel.updateItem(
                todoItem!!.copy(
                    text = text,
                    importance = importance,
                    deadline = deadline,
                    updateDate = Date().time
                )
            )
        } else {
            viewModel.addItem(
                TodoItem(
                    id = "1",
                    text = text,
                    importance = importance,
                    deadline = deadline,
                    isCompleted = false,
                    creationDate = Date().time
                )
            )
        }
    }

    private fun showDatepickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                selectedDeadline = selectedCalendar.timeInMillis
                binding.selectedDeadline.text = selectedDeadline.toStringDate()
            },
            year, month, day
        )
        datePickerDialog.show()
    }

}