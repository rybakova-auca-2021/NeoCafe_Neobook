package com.example.neocafe.room

class ShoppingCartRepository(private val productDao: ProductDao) {
    suspend fun insertProduct(product: Product) {
        productDao.insertCartItem(product)
    }

    suspend fun getAllProducts(): List<Product> {
        return productDao.getAllCartItems()
    }
}
