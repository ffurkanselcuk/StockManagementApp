package com.fs.stockmanagementapp.ui.product

import androidx.lifecycle.*
import com.fs.stockmanagementapp.data.model.Product
import com.fs.stockmanagementapp.data.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    private val repository: InventoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: Int = savedStateHandle.get<Int>("productId") ?: -1

    val isEditMode = productId != -1

    val product: LiveData<Product?> = if (isEditMode) {
        repository.getProductById(productId).asLiveData()
    } else {
        MutableLiveData(null)
    }

    private val _saveEvent = MutableSharedFlow<Unit>()
    val saveEvent = _saveEvent.asSharedFlow()

    fun saveProduct(
        name: String,
        priceStr: String,
        category: String,
        currentStockStr: String,
        minStockStr: String,
        barcode: String?,
        description: String?
    ) {
        if (name.isBlank() || priceStr.isBlank() || category.isBlank() || currentStockStr.isBlank() || minStockStr.isBlank()) {
            return
        }

        val price = priceStr.toDoubleOrNull() ?: 0.0
        val currentStock = currentStockStr.toIntOrNull() ?: 0
        val minStock = minStockStr.toIntOrNull() ?: 0

        val productToSave = if (isEditMode) {
            product.value!!.copy(
                name = name,
                price = price,
                category = category,
                currentStockLevel = currentStock,
                minimumStockLevel = minStock,
                barcode = barcode,
                description = description
            )
        } else {
            Product(
                name = name,
                price = price,
                category = category,
                currentStockLevel = currentStock,
                minimumStockLevel = minStock,
                barcode = barcode,
                description = description,
                supplierId = null
            )
        }

        viewModelScope.launch {
            repository.saveProduct(productToSave)
            _saveEvent.emit(Unit)
        }
    }
}