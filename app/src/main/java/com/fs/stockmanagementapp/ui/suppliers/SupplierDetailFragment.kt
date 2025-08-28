package com.fs.stockmanagementapp.ui.suppliers

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fs.stockmanagementapp.R
import com.fs.stockmanagementapp.data.model.Supplier
import com.fs.stockmanagementapp.databinding.FragmentSupplierDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SupplierDetailFragment : Fragment() {

    private var _binding: FragmentSupplierDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SupplierDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupplierDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        viewModel.supplier.observe(viewLifecycleOwner) { supplier ->
            supplier?.let { bindDetails(it) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteEvent.collectLatest {
                Toast.makeText(context, "Supplier deleted successfully.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun bindDetails(supplier: Supplier) {
        binding.textViewSupplierName.text = supplier.name
        binding.textViewContactPerson.text = supplier.contactPerson ?: "Unspecified"
        binding.textViewPhone.text = supplier.phone ?: "Unspecified"
        binding.textViewEmail.text = supplier.email ?: "Unspecified"
        binding.textViewAddress.text = supplier.address ?: "Unspecified"
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit -> {
                        viewModel.supplier.value?.let {
                            val action = SupplierDetailFragmentDirections
                                .actionSupplierDetailFragmentToAddEditSupplierFragment(it.id)
                            findNavController().navigate(action)
                        }
                        true
                    }
                    R.id.action_delete -> {
                        showDeleteConfirmationDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Supplier")
            .setMessage("Are you sure you want to delete this supplier? This action is irreversible.")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteSupplier()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}