package com.fs.stockmanagementapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fs.stockmanagementapp.data.model.Product
import com.fs.stockmanagementapp.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.*

class ProductListAdapter(private val onItemClicked: (Product) -> Unit) :
    ListAdapter<Product, ProductListAdapter.ProductViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ProductViewHolder(private var binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.textViewProductName.text = product.name
            binding.textViewProductStock.text = "Stock: ${product.currentStockLevel}"
            val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
            binding.textViewProductPrice.text = format.format(product.price)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
        }
    }
}