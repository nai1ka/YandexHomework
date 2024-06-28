package ru.ndevelop.yandexhomework.presentation.adapters.viewholders

import android.graphics.Paint
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.core.TodoItem
import ru.ndevelop.yandexhomework.core.TodoItemImportance
import ru.ndevelop.yandexhomework.core.listeners.OnItemClickListener
import ru.ndevelop.yandexhomework.core.toStringDate
import ru.ndevelop.yandexhomework.databinding.ItemCellBinding

class ItemViewHolder(
    private val binding: ItemCellBinding,
    private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position)
                }
            }
            binding.checkBox.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemCheckedChange(
                        adapterPosition, binding.checkBox.isChecked
                    )
                }
            }
        }

        fun bind(item: TodoItem) = with(binding) {
            text.apply {
                text = item.text
                paintFlags = if (item.isCompleted) {
                    paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
            checkBox.setCompleted(item.isCompleted)

            if (item.deadline != null) {
                textDeadline.isVisible = true
                textDeadline.text = item.deadline.toStringDate()
            } else {
                textDeadline.isVisible = false
            }

            when (item.importance) {
                TodoItemImportance.LOW -> {
                    text.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_low_priority, 0, 0, 0
                    )
                    checkBox.isUrgent = false
                }

                TodoItemImportance.HIGH -> {
                    text.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_high_priority, 0, 0, 0
                    )
                    checkBox.isUrgent = true
                }

                TodoItemImportance.NORMAL -> {
                    text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    checkBox.isUrgent = false
                }
            }
        }
    }