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
class ProductDetailViewModel @Inject constructor(
    private val repository: InventoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val productId: Int = savedStateHandle.get<Int>("productId")!!

    val product: LiveData<Product?> = repository.getProductById(productId).asLiveData()

    private val _deleteEvent = MutableSharedFlow<Unit>()
    val deleteEvent = _deleteEvent.asSharedFlow()

    fun deleteProduct() {
        viewModelScope.launch {
            product.value?.let {
                repository.deleteProduct(it)
                _deleteEvent.emit(Unit)
            }
        }
    }

    fun recordSale(quantity: Int, notes: String?) {
        product.value?.let {
            viewModelScope.launch {
                repository.recordSale(it, quantity, notes)
            }
        }
    }

    fun recordRestock(quantity: Int, notes: String?) {
        product.value?.let {
            viewModelScope.launch {
                repository.recordRestock(it, quantity, notes)
            }
        }
    }
}