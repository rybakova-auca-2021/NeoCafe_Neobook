package com.example.neocafe.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: Product)

    @Query("SELECT * FROM ProductDB")
    suspend fun getAllCartItems(): List<Product>

    @Delete
    fun deleteProducts(products: List<Product>)


    @Query("SELECT * FROM ProductDB WHERE id = :productId")
    suspend fun getProductById(productId: Int): Product?

    @Query("UPDATE ProductDB SET quantity = :newQuantity WHERE id = :productId")
    suspend fun updateProductQuantity(newQuantity: Int, productId: Int)

    @Query("UPDATE ProductDB SET quantity = quantity + :quantityDelta WHERE id = :productId")
    suspend fun increaseProductQuantity(productId: Int, quantityDelta: Int)

    @Query("UPDATE ProductDB SET quantity = quantity - :quantityDelta WHERE id = :productId AND quantity >= :quantityDelta")
    suspend fun decreaseProductQuantity(productId: Int, quantityDelta: Int): Int

    @Query("DELETE FROM ProductDB WHERE id = :productId")
    suspend fun deleteProductById(productId: Int)
}
