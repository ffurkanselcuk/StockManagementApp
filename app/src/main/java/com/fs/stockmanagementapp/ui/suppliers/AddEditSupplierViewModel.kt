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
class AddEditSupplierViewModel @Inject constructor(
    private val repository: InventoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val supplierId: Int = savedStateHandle.get<Int>("supplierId") ?: -1
    val isEditMode = supplierId != -1

    val supplier: LiveData<Supplier?> = if (isEditMode) {
        repository.getSupplierById(supplierId).asLiveData()
    } else {
        MutableLiveData(null)
    }

    private val _saveEvent = MutableSharedFlow<Unit>()
    val saveEvent = _saveEvent.asSharedFlow()

    fun saveSupplier(
        name: String,
        contactPerson: String?,
        phone: String?,
        email: String?,
        address: String?
    ) {
        if (name.isBlank()) {
            return
        }

        val supplierToSave = if (isEditMode) {
            supplier.value!!.copy(
                name = name,
                contactPerson = contactPerson,
                phone = phone,
                email = email,
                address = address
            )
        } else {
            Supplier(
                name = name,
                contactPerson = contactPerson,
                phone = phone,
                email = email,
                address = address
            )
        }

        viewModelScope.launch {
            repository.saveSupplier(supplierToSave)
            _saveEvent.emit(Unit)
        }
    }
}