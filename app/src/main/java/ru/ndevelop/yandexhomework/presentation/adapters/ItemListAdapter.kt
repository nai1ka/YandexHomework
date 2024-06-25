package ru.ndevelop.yandexhomework.presentation.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.core.ItemsDiff
import ru.ndevelop.yandexhomework.core.listeners.OnItemClickListener
import ru.ndevelop.yandexhomework.core.toStringDate
import ru.ndevelop.yandexhomework.data.TodoItem
import ru.ndevelop.yandexhomework.data.TodoItemImportance
import ru.ndevelop.yandexhomework.databinding.ItemCellBinding
import ru.ndevelop.yandexhomework.databinding.ItemNewBinding

class ItemListAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<TodoItem, RecyclerView.ViewHolder>(ItemsDiff()) {

    class ItemViewHolder(
        private val binding: ItemCellBinding, private val onItemClickListener: OnItemClickListener
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

        fun bind(item: TodoItem) {
            binding.text.apply {
                text = item.text
                paintFlags = if (item.isCompleted) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            binding.checkBox.setCompleted(item.isCompleted)

            if (item.deadline != null) {
                binding.textDeadline.isVisible = true
                binding.textDeadline.text = item.deadline.toStringDate()
            } else {
                binding.textDeadline.isVisible = false
            }

            when (item.importance) {
                TodoItemImportance.LOW -> {
                    binding.text.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_low_priority, 0, 0, 0
                    )
                    binding.checkBox.isUrgent = false
                }

                TodoItemImportance.HIGH -> {
                    binding.text.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_high_priority, 0, 0, 0
                    )
                    binding.checkBox.isUrgent = true
                }

                TodoItemImportance.NORMAL -> {
                    binding.text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    binding.checkBox.isUrgent = false
                }
            }
        }
    }

    class AddNewItemHolder(
        binding: ItemNewBinding, private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onNewItemClick()
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (ItemType.entries[viewType]) {
            ItemType.TYPE_ITEM -> {
                val itemBinding =
                    ItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(itemBinding, onItemClickListener)
            }

            ItemType.TYPE_ADD_ITEM -> {
                val itemBinding =
                    ItemNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return AddNewItemHolder(itemBinding, onItemClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (ItemType.entries[getItemViewType(position)]) {
            ItemType.TYPE_ITEM -> (holder as ItemViewHolder).bind(currentList[position])
            ItemType.TYPE_ADD_ITEM -> (holder as AddNewItemHolder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size) {
            ItemType.TYPE_ADD_ITEM.ordinal
        } else {
            ItemType.TYPE_ITEM.ordinal
        }
    }

    override fun getItemCount() = currentList.size + 1

    enum class ItemType {
        TYPE_ITEM, TYPE_ADD_ITEM
    }
}