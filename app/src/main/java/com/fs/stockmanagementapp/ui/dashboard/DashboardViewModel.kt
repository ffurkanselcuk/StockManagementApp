package com.fs.stockmanagementapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fs.stockmanagementapp.data.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: InventoryRepository
) : ViewModel() {
    val lowStockProducts = repository.getLowStockProducts().asLiveData()
    val topStockProducts = repository.getTopStockProducts().asLiveData()
}