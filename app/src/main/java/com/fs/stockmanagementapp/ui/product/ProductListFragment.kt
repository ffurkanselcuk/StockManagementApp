package com.fs.stockmanagementapp.ui.product

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
import com.fs.stockmanagementapp.databinding.FragmentProductListBinding
import com.fs.stockmanagementapp.ui.adapters.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ProductListAdapter { product ->
            val action = ProductListFragmentDirections.actionProductListFragmentToProductDetailFragment(product.id)
            findNavController().navigate(action)
        }
        binding.recyclerViewProducts.adapter = adapter

        binding.editTextSearch.doAfterTextChanged { text ->
            viewModel.onSearchQueryChanged(text.toString())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    adapter.submitList(state.products)

                    if (!state.isLoading) {
                        binding.textViewEmptyState.isVisible = state.products.isEmpty()
                        if (state.products.isEmpty() && viewModel.searchQuery.value.isNotEmpty()) {
                            binding.textViewEmptyState.text = "Search results not found."
                        } else {
                            binding.textViewEmptyState.text = "No products added yet."
                        }
                    }
                }
            }
        }

        binding.fabAddProduct.setOnClickListener {
            val action = ProductListFragmentDirections.actionProductListFragmentToAddEditProductFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}