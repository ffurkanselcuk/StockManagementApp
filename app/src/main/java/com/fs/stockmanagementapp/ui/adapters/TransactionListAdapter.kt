package com.fs.stockmanagementapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fs.stockmanagementapp.data.model.Transaction
import com.fs.stockmanagementapp.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionListAdapter :
    ListAdapter<Transaction, TransactionListAdapter.TransactionViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TransactionViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr"))

        fun bind(transaction: Transaction) {
            val typeText = if (transaction.type == "SALE") "SELL" else "STOCK ENTRY"
            val infoText = "$typeText: ${transaction.quantity} unit ${transaction.productName}"
            binding.textViewTransactionInfo.text = infoText

            if (transaction.type == "SALE") {
                binding.textViewTransactionInfo.setTextColor(Color.parseColor("#D32F2F")) // Kırmızı
            } else {
                binding.textViewTransactionInfo.setTextColor(Color.parseColor("#388E3C")) // Yeşil
            }

            binding.textViewTransactionDate.text = dateFormat.format(Date(transaction.date))

            if (transaction.notes.isNullOrBlank()) {
                binding.textViewTransactionNotes.visibility = View.GONE
            } else {
                binding.textViewTransactionNotes.visibility = View.VISIBLE
                binding.textViewTransactionNotes.text = "Note: ${transaction.notes}"
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) = oldItem == newItem
        }
    }
}