package com.fs.stockmanagementapp.ui.suppliers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fs.stockmanagementapp.data.model.Supplier
import com.fs.stockmanagementapp.data.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SupplierListUiState(
    val isLoading: Boolean = true,
    val suppliers: List<Supplier> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SupplierListViewModel @Inject constructor(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(SupplierListUiState())
    val uiState: StateFlow<SupplierListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            searchQuery.flatMapLatest { query ->
                repository.getAllSuppliers(query)
            }.catch { e ->
                _uiState.value = SupplierListUiState(isLoading = false, error = e.message)
            }.collect { suppliers ->
                _uiState.value = SupplierListUiState(isLoading = false, suppliers = suppliers)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}