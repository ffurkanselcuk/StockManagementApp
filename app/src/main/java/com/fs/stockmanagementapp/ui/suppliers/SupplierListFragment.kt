package com.fs.stockmanagementapp.ui.suppliers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.fs.stockmanagementapp.databinding.FragmentSupplierListBinding
import com.fs.stockmanagementapp.ui.adapters.SupplierListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SupplierListFragment : Fragment() {

    private var _binding: FragmentSupplierListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SupplierListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupplierListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SupplierListAdapter { supplier ->
            val action = SupplierListFragmentDirections
                .actionSupplierListFragmentToSupplierDetailFragment(supplier.id)
            findNavController().navigate(action)
        }
        binding.recyclerViewSuppliers.adapter = adapter

        binding.editTextSearch.doAfterTextChanged { text ->
            viewModel.onSearchQueryChanged(text.toString())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    adapter.submitList(state.suppliers)

                    if (!state.isLoading) {
                        binding.textViewEmptyState.isVisible = state.suppliers.isEmpty()
                        if (state.suppliers.isEmpty() && viewModel.searchQuery.value.isNotEmpty()) {
                            binding.textViewEmptyState.text = "Search results not found."
                        } else {
                            binding.textViewEmptyState.text = "No suppliers added yet."
                        }
                    }
                }
            }
        }

        binding.fabAddSupplier.setOnClickListener {
            val action = SupplierListFragmentDirections
                .actionSupplierListFragmentToAddEditSupplierFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}