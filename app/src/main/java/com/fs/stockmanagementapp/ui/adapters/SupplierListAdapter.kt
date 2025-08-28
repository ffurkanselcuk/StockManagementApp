package com.fs.stockmanagementapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fs.stockmanagementapp.data.model.Supplier
import com.fs.stockmanagementapp.databinding.ItemSupplierBinding

class SupplierListAdapter(private val onItemClicked: (Supplier) -> Unit) :
    ListAdapter<Supplier, SupplierListAdapter.SupplierViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        return SupplierViewHolder(
            ItemSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(current) }
        holder.bind(current)
    }

    class SupplierViewHolder(private var binding: ItemSupplierBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(supplier: Supplier) {
            binding.textViewSupplierName.text = supplier.name
            binding.textViewSupplierContact.text = "${supplier.contactPerson ?: ""} - ${supplier.phone ?: ""}"
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Supplier>() {
            override fun areItemsTheSame(oldItem: Supplier, newItem: Supplier) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Supplier, newItem: Supplier) = oldItem == newItem
        }
    }
}