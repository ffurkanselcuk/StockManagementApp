package com.fs.stockmanagementapp.ui.suppliers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fs.stockmanagementapp.data.model.Supplier
import com.fs.stockmanagementapp.databinding.FragmentAddEditSupplierBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditSupplierFragment : Fragment() {

    private var _binding: FragmentAddEditSupplierBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddEditSupplierViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditSupplierBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isEditMode) {
            viewModel.supplier.observe(viewLifecycleOwner) { supplier ->
                supplier?.let { populateForm(it) }
            }
        }

        binding.buttonSave.setOnClickListener {
            saveSupplier()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.saveEvent.collectLatest {
                findNavController().popBackStack()
            }
        }
    }

    private fun populateForm(supplier: Supplier) {
        binding.apply {
            editTextSupplierName.setText(supplier.name)
            editTextContactPerson.setText(supplier.contactPerson)
            editTextPhone.setText(supplier.phone)
            editTextEmail.setText(supplier.email)
            editTextAddress.setText(supplier.address)
        }
    }

    private fun saveSupplier() {
        val name = binding.editTextSupplierName.text.toString().trim()
        val contactPerson = binding.editTextContactPerson.text.toString().trim()
        val phone = binding.editTextPhone.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val address = binding.editTextAddress.text.toString().trim()

        if (name.isBlank()) {
            Toast.makeText(context, "Supplier name cannot be left blank.", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.saveSupplier(name, contactPerson, phone, email, address)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}