package com.fs.stockmanagementapp.data.model



import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "suppliers")
data class Supplier(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val contactPerson: String?,
    val phone: String?,
    val email: String?,
    val address: String?
) : Serializable