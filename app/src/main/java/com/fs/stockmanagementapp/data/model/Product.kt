package com.fs.stockmanagementapp.data.model



import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "products",
    foreignKeys = [ForeignKey(
        entity = Supplier::class,
        parentColumns = ["id"],
        childColumns = ["supplierId"],
        onDelete = ForeignKey.SET_NULL
    )]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String?,
    val price: Double,
    val category: String,
    val barcode: String?,
    val supplierId: Int?,
    var currentStockLevel: Int,
    val minimumStockLevel: Int
) : Serializable