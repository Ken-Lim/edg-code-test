package com.example.edg.data.repository

import com.example.edg.common.Error
import com.example.edg.common.Result
import com.example.edg.common.Success
import com.example.edg.data.domain.model.Product
import com.example.edg.data.domain.toProduct
import com.example.edg.data.network.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(private val productService: ProductService) : ProductRepository {
    private val _productsList: MutableList<Product> = mutableListOf()
    private val productsList get() = _productsList.toList()

    override suspend fun getProducts(): Result<List<Product>> {
        return if (_productsList.isNotEmpty()) {
            Success(_productsList)
        } else {
            try {
                val result = withContext(Dispatchers.IO) {
                    productService.getProducts()
                }
                _productsList.clear()
                _productsList.addAll(result.products.map { it.toProduct() })
                Success(productsList)
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    override fun getFavouriteProducts(): List<Product> {
        return productsList.filter { it.isFavourite }
    }

    override fun toggleFavouriteForProduct(productId: String): Product {
        val product = _productsList.first { it.id == productId }
        product.isFavourite = !product.isFavourite
        return product
    }
}
