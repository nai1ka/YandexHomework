package ru.ndevelop.yandexhomework.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.ndevelop.yandexhomework.core.TodoItem
import ru.ndevelop.yandexhomework.core.listeners.OnItemClickListener
import ru.ndevelop.yandexhomework.databinding.ItemCellBinding
import ru.ndevelop.yandexhomework.databinding.ItemNewBinding
import ru.ndevelop.yandexhomework.presentation.adapters.viewholders.AddNewItemHolder
import ru.ndevelop.yandexhomework.presentation.adapters.viewholders.ItemViewHolder

class ItemListAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<TodoItem, RecyclerView.ViewHolder>(ItemsDiff()) {

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
        when (holder) {
            is ItemViewHolder -> holder.bind(currentList[position])
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