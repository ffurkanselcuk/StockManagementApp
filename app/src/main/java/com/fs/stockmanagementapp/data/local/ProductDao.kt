package com.fs.stockmanagementapp.data.local

import androidx.room.*
import com.fs.stockmanagementapp.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun getAllProducts(searchQuery: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: Int): Flow<Product?>

    @Query("SELECT * FROM products WHERE currentStockLevel <= minimumStockLevel")
    fun getLowStockProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY currentStockLevel DESC LIMIT 5")
    fun getTopStockProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(product: Product)

    @Delete
    suspend fun delete(product: Product)
}