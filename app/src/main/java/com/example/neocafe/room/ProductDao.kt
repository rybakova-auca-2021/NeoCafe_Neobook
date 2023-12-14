package com.example.neocafe.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: Product)

    @Query("SELECT * FROM ProductDB")
    suspend fun getAllCartItems(): List<Product>
}
