package ru.ndevelop.yandexhomework.presentation.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import ru.ndevelop.yandexhomework.core.listeners.OnItemClickListener
import ru.ndevelop.yandexhomework.databinding.ItemNewBinding

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