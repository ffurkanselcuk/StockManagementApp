package com.fs.stockmanagementapp.data.repository

import com.fs.stockmanagementapp.data.local.ProductDao
import com.fs.stockmanagementapp.data.local.SupplierDao
import com.fs.stockmanagementapp.data.local.TransactionDao
import com.fs.stockmanagementapp.data.model.Product
import com.fs.stockmanagementapp.data.model.Supplier
import com.fs.stockmanagementapp.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepository @Inject constructor(
    private val productDao: ProductDao,
    private val supplierDao: SupplierDao,
    private val transactionDao: TransactionDao
) {
    fun getAllProducts(searchQuery: String): Flow<List<Product>> =
        productDao.getAllProducts(searchQuery)

    fun getLowStockProducts(): Flow<List<Product>> = productDao.getLowStockProducts()
    fun getProductById(id: Int): Flow<Product?> = productDao.getProductById(id)
    fun getTopStockProducts(): Flow<List<Product>> = productDao.getTopStockProducts()
    suspend fun saveProduct(product: Product) = productDao.insertOrUpdate(product)
    suspend fun deleteProduct(product: Product) = productDao.delete(product)

    fun getAllSuppliers(searchQuery: String): Flow<List<Supplier>> =
        supplierDao.getAllSuppliers(searchQuery)

    fun getSupplierById(id: Int): Flow<Supplier?> = supplierDao.getSupplierById(id)
    suspend fun saveSupplier(supplier: Supplier) = supplierDao.insertOrUpdate(supplier)
    suspend fun deleteSupplier(supplier: Supplier) = supplierDao.delete(supplier)

    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun recordSale(product: Product, quantity: Int, notes: String?) {
        val updatedProduct = product.copy(currentStockLevel = product.currentStockLevel - quantity)
        productDao.insertOrUpdate(updatedProduct)
        val transaction = Transaction(
            type = "SALE",
            productId = product.id,
            productName = product.name,
            quantity = quantity,
            notes = notes
        )
        transactionDao.insert(transaction)
    }

    suspend fun recordRestock(product: Product, quantity: Int, notes: String?) {
        val updatedProduct = product.copy(currentStockLevel = product.currentStockLevel + quantity)
        productDao.insertOrUpdate(updatedProduct)
        val transaction = Transaction(
            type = "RESTOCK",
            productId = product.id,
            productName = product.name,
            quantity = quantity,
            notes = notes
        )
        transactionDao.insert(transaction)
    }
}