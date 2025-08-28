package com.fs.stockmanagementapp.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fs.stockmanagementapp.R
import com.fs.stockmanagementapp.data.model.Product
import com.fs.stockmanagementapp.databinding.FragmentAddEditProductBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditProductFragment : Fragment() {

    private var _binding: FragmentAddEditProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddEditProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("barcode_request") { _, bundle ->
            val scannedBarcode = bundle.getString("scanned_barcode")
            binding.editTextBarcode.setText(scannedBarcode)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isEditMode) {
            viewModel.product.observe(viewLifecycleOwner) { product ->
                product?.let { populateForm(it) }
            }
        }

        binding.textInputLayoutBarcode.setEndIconOnClickListener {
            findNavController().navigate(R.id.action_addEditProductFragment_to_barcodeScannerFragment)
        }

        binding.buttonSave.setOnClickListener {
            saveProduct()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.saveEvent.collectLatest {
                findNavController().popBackStack()
            }
        }
    }

    private fun populateForm(product: Product) {
        binding.apply {
            editTextProductName.setText(product.name)
            editTextPrice.setText(product.price.toString())
            editTextCategory.setText(product.category)
            editTextCurrentStock.setText(product.currentStockLevel.toString())
            editTextMinStock.setText(product.minimumStockLevel.toString())
            editTextBarcode.setText(product.barcode)
            editTextDescription.setText(product.description)
        }
    }

    private fun saveProduct() {
        val name = binding.editTextProductName.text.toString().trim()
        val price = binding.editTextPrice.text.toString().trim()
        val category = binding.editTextCategory.text.toString().trim()
        val currentStock = binding.editTextCurrentStock.text.toString().trim()
        val minStock = binding.editTextMinStock.text.toString().trim()
        val barcode = binding.editTextBarcode.text.toString().trim()
        val description = binding.editTextDescription.text.toString().trim()

        if (name.isBlank() || price.isBlank() || category.isBlank() || currentStock.isBlank() || minStock.isBlank()) {
            Toast.makeText(context, "Please fill in the starred fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.saveProduct(name, price, category, currentStock, minStock, barcode, description)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}