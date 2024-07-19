package ru.ndevelop.yandexhomework.presentation.screens.itemList

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.ndevelop.yandexhomework.R
import ru.ndevelop.yandexhomework.appComponent
import ru.ndevelop.yandexhomework.core.RecyclerTouchCallback
import ru.ndevelop.yandexhomework.core.listeners.OnItemClickListener
import ru.ndevelop.yandexhomework.core.listeners.OnItemSwipeListener
import ru.ndevelop.yandexhomework.databinding.FragmentItemsListBinding
import ru.ndevelop.yandexhomework.presentation.ItemListUiEffect
import ru.ndevelop.yandexhomework.presentation.LceState
import ru.ndevelop.yandexhomework.presentation.adapters.ItemListAdapter
import ru.ndevelop.yandexhomework.presentation.adapters.viewholders.AddNewItemHolder
import ru.ndevelop.yandexhomework.presentation.screens.addItem.AddItemFragment
import ru.ndevelop.yandexhomework.presentation.viewmodels.Factory
import ru.ndevelop.yandexhomework.presentation.viewmodels.ItemListViewModel

class ItemListFragment : Fragment() {
    private var _binding: FragmentItemsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemListViewModel by viewModels {
        Factory {
            requireContext().appComponent().itemListViewModel
        }
    }
    private val networkRequest =
        NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build()
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            viewModel.synchronizeItems()
            viewModel.fetchItems()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Snackbar.make(binding.root, getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val connectivityManager =
                requireContext().getSystemService(ConnectivityManager::class.java)
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemsListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()

    }

    private val onItemClickListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(index: Int) {
            val action =
                ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(adapter.currentList[index])
            findNavController().navigate(action)
        }

        override fun onItemCheckedChange(index: Int, isChecked: Boolean) {
            viewModel.changeCompletedState(adapter.currentList[index], isChecked, index)
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
        setFragmentResultListener(AddItemFragment.REQUEST_KEY) { key, bundle ->
            if (bundle.getBoolean("refresh")) {
                viewModel.fetchItems()
            }
        }
    }

    private fun setupUI() = with(binding) {
        collapsingToolbar.title = getString(R.string.my_items_title)
        btnInfo.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToAboutAppFragment()
            findNavController().navigate(action)
        }

        btnSettings.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToSettingsFragment()
            findNavController().navigate(action)
        }


        appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            toolbarFrameLayout.isVisible = (verticalOffset == 0)
        }


        swiperefresh.setOnRefreshListener {
            viewModel.fetchItems()
        }

        errorScreen.retryButton.setOnClickListener {
            viewModel.fetchItems()
        }

        changeVisibilityButton.setOnClickListener {
            viewModel.areCompletedItemsVisible = !viewModel.areCompletedItemsVisible
            val iconDrawable = if (viewModel.areCompletedItemsVisible) {
                R.drawable.ic_visibility
            } else {
                R.drawable.ic_visibility_off
            }
            changeVisibilityButton.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), iconDrawable)
            )
        }

        recyclerView.adapter = adapter

        floatingActionBar.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(null)
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
            ).collect { state ->
                renderState(state)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEffect.flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
            ).collect { effect ->
                handleEffect(effect)
            }
        }
        val onItemSwipeListener = object : OnItemSwipeListener {
            override fun onLeftSwipe(index: Int) {
                viewModel.deleteItem(adapter.currentList[index], index)
            }

            override fun onRightSwipe(index: Int) {
                val item = adapter.currentList[index]
                viewModel.changeCompletedState(item, true, index)
            }
        }
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
            object : RecyclerTouchCallback(
                requireActivity().applicationContext, onItemSwipeListener
            ) {
                override fun getSwipeDirs(
                    recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
                ): Int {
                    if (viewHolder is AddNewItemHolder) return 0
                    val position = viewHolder.adapterPosition
                    if (position != RecyclerView.NO_POSITION && adapter.currentList[position].isCompleted) {
                        return ItemTouchHelper.LEFT
                    }
                    return super.getSwipeDirs(recyclerView, viewHolder)
                }
            }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun renderState(state: LceState<ItemListUiState>) {
        when (state) {
            is LceState.Loading -> {
                binding.swiperefresh.isRefreshing = true
                binding.cardViewList.isVisible = false
                binding.skeletonScreen.root.isVisible = true
                binding.errorScreen.root.isVisible = false
            }

            is LceState.Content -> {
                val data = state.requireData()
                binding.labelCompleted.text =
                    getString(R.string.label_completed, data.numberOfCompletedItems)
                adapter.submitList(data.items)
                binding.cardViewList.isVisible = true
                binding.swiperefresh.isRefreshing = false
                binding.errorScreen.root.isVisible = false
                binding.skeletonScreen.root.isVisible = false
            }

            is LceState.Error -> {
                binding.cardViewList.isVisible = false
                binding.errorScreen.root.isVisible = true
                binding.swiperefresh.isRefreshing = false
                binding.skeletonScreen.root.isVisible = false
            }
        }

    }



    private fun handleEffect(effect: ItemListUiEffect) {
        when (effect) {
            is ItemListUiEffect.ShowError -> {
                Toast.makeText(requireContext(), effect.errorMessage, Toast.LENGTH_SHORT).show()
                if (effect.itemPosition != null) {
                    adapter.notifyItemChanged(effect.itemPosition)
                }

            }
        }
    }
}
