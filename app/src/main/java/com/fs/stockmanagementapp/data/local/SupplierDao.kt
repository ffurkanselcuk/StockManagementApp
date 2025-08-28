package com.fs.stockmanagementapp.data.local

import androidx.room.*
import com.fs.stockmanagementapp.data.model.Supplier
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {
    @Query("SELECT * FROM suppliers WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun getAllSuppliers(searchQuery: String): Flow<List<Supplier>>

    @Query("SELECT * FROM suppliers WHERE id = :supplierId")
    fun getSupplierById(supplierId: Int): Flow<Supplier?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(supplier: Supplier)

    @Delete
    suspend fun delete(supplier: Supplier)
}