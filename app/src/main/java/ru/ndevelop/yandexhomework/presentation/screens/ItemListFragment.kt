package ru.ndevelop.yandexhomework.presentation.screens

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.core.listeners.OnItemClickListener
import ru.ndevelop.yandexhomework.databinding.FragmentItemsListBinding
import ru.ndevelop.yandexhomework.presentation.adapters.ItemListAdapter
import ru.ndevelop.yandexhomework.presentation.viewmodels.ItemListViewModel

class ItemListFragment : Fragment() {
    private lateinit var binding: FragmentItemsListBinding
    private val viewModel: ItemListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val onItemClickListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(index: Int) {
            val action =
                ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(adapter.currentList[index])
            findNavController().navigate(action)
        }

        override fun onItemCheckedChange(index: Int, isChecked: Boolean) {
            viewModel.changeCompletedState(adapter.currentList[index], isChecked)
        }

        override fun onNewItemClick() {
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(null)
            findNavController().navigate(action)
        }
    }
    val adapter = ItemListAdapter(onItemClickListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.collapsingToolbar.title = getString(R.string.my_items_title)
        binding.appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            binding.toolbarFrameLayout.isVisible = verticalOffset == 0
        }

        binding.changeVisibilityButton.setOnClickListener {
            viewModel.doneItemsVisible = !viewModel.doneItemsVisible
            if (viewModel.doneItemsVisible) {
                binding.changeVisibilityButton.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_visibility)
                )
            } else {
                binding.changeVisibilityButton.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_visibility_off)
                )
            }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.floatingActionBar.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(null)
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dataState.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect {
                adapter.submitList(it)
                binding.labelCompleted.text = "Выполнено - ${viewModel.numberOfCompletedItems}"
            }
        }


        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.removeItem(adapter.currentList[viewHolder.adapterPosition])
                } else {
                    val item = adapter.currentList[viewHolder.adapterPosition]
                    viewModel.changeCompletedState(item, true)
                }
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder is ItemListAdapter.AddNewItemHolder) return 0
                return super.getSwipeDirs(recyclerView, viewHolder)
            }

            val redPaint = Paint().apply {
                color = ContextCompat.getColor(requireContext(), R.color.red)
            }

            val greenPaint = Paint().apply {
                color = ContextCompat.getColor(requireContext(), R.color.green)
            }

            val deleteIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
            val doneButton = ContextCompat.getDrawable(requireContext(), R.drawable.ic_done)
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView

                    // Draw red background on swipe
                    if (dX < 0) { // Swiping to the left
                        c.drawRect(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat(),
                            redPaint
                        )

                        deleteIcon?.let {
                            val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                            val iconTop = itemView.top + iconMargin
                            val iconBottom = iconTop + it.intrinsicHeight
                            val iconLeft = itemView.right + dX.toInt() + iconMargin
                            val iconRight =
                                itemView.right + dX.toInt() + iconMargin + it.intrinsicWidth

                            if (iconRight < itemView.right) {
                                it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                                it.draw(c)
                            }
                        }
                    } else if (dX > 0) {
                        c.drawRect(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            itemView.left.toFloat() + dX,
                            itemView.bottom.toFloat(),
                            greenPaint
                        )

                        doneButton?.let {
                            val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                            val iconTop = itemView.top + iconMargin
                            val iconBottom = iconTop + it.intrinsicHeight
                            val iconRight = itemView.left + dX.toInt() - iconMargin
                            val iconLeft = iconRight - it.intrinsicWidth
                            if (iconLeft >= itemView.left) {
                                it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                                it.draw(c)
                            }
                        }
                    }

                    super.onChildDraw(c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
}
