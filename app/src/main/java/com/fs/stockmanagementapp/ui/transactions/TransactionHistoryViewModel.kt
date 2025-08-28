package com.fs.stockmanagementapp.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fs.stockmanagementapp.data.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    repository: InventoryRepository
) : ViewModel() {

    val allTransactions = repository.getAllTransactions().asLiveData()

}