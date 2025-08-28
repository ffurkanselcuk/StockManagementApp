package com.fs.stockmanagementapp.ui.product

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fs.stockmanagementapp.R
import com.fs.stockmanagementapp.data.model.Product
import com.fs.stockmanagementapp.databinding.FragmentProductDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        viewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let { bindProductDetails(it) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteEvent.collectLatest {
                Toast.makeText(context, "The product was deleted successfully.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        binding.buttonRecordSale.setOnClickListener {
            showStockDialog("Sell", "Quantity Sold", isSale = true)
        }
        binding.buttonAddStock.setOnClickListener {
            showStockDialog("Add Stock", "Amount Added", isSale = false)
        }
    }

    private fun bindProductDetails(product: Product) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
        binding.textViewProductName.text = product.name
        binding.textViewProductPrice.text = format.format(product.price)
        binding.textViewCurrentStock.text = "Current Stock: ${product.currentStockLevel} unit"
        binding.textViewMinStock.text = "Minimum Stock: ${product.minimumStockLevel} unit"
        val details = """
            Barcode: ${product.barcode ?: "-"}
            Category: ${product.category}
            Explanation: ${product.description ?: "-"}
        """.trimIndent()
        binding.textViewProductDetails.text = details
    }

    private fun showStockDialog(title: String, hint: String, isSale: Boolean) {
        val editText = EditText(requireContext()).apply {
            this.hint = hint
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(editText)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Accept") { _, _ ->
                val quantityStr = editText.text.toString()
                if (quantityStr.isNotBlank()) {
                    val quantity = quantityStr.toInt()
                    if (isSale) {
                        if (quantity > (viewModel.product.value?.currentStockLevel ?: 0)) {
                            Toast.makeText(context, "Low stock!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.recordSale(quantity, null)
                        }
                    } else {
                        viewModel.recordRestock(quantity, null)
                    }
                }
            }
            .show()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit -> {
                        viewModel.product.value?.let {
                            val action = ProductDetailFragmentDirections
                                .actionProductDetailFragmentToAddEditProductFragment(it.id)
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
            .setTitle("Delete product")
            .setMessage("Are you sure you want to delete this product? This action cannot be undone..")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteProduct()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}