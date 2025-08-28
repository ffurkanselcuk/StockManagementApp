package com.fs.stockmanagementapp.ui.suppliers

import androidx.lifecycle.*
import com.fs.stockmanagementapp.data.model.Supplier
import com.fs.stockmanagementapp.data.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierDetailViewModel @Inject constructor(
    private val repository: InventoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val supplierId: Int = savedStateHandle.get<Int>("supplierId")!!

    val supplier: LiveData<Supplier?> = repository.getSupplierById(supplierId).asLiveData()

    private val _deleteEvent = MutableSharedFlow<Unit>()
    val deleteEvent = _deleteEvent.asSharedFlow()

    fun deleteSupplier() {
        viewModelScope.launch {
            supplier.value?.let {
                repository.deleteSupplier(it)
                _deleteEvent.emit(Unit)
            }
        }
    }
}