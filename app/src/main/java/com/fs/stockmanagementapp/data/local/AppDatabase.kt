package com.fs.stockmanagementapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fs.stockmanagementapp.data.model.Product
import com.fs.stockmanagementapp.data.model.Supplier
import com.fs.stockmanagementapp.data.model.Transaction

@Database(entities = [Product::class, Supplier::class, Transaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun supplierDao(): SupplierDao
    abstract fun transactionDao(): TransactionDao
}