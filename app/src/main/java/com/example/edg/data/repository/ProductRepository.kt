package com.example.edg.data.repository

import com.example.edg.common.Result
import com.example.edg.data.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    fun getFavouriteProducts(): List<Product>
    fun toggleFavouriteForProduct(productId: String): Product
}
