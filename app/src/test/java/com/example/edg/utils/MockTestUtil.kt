package com.example.edg.utils

import com.example.edg.data.domain.model.Product
import com.example.edg.data.domain.toProduct
import com.example.edg.data.network.model.ProductsResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MockTestUtil {
    fun mockProductsResponse(): ProductsResponse {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("products.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        val type = object : TypeToken<ProductsResponse>() {}.type
        return Gson().fromJson(json, type)
    }

    fun mockProductsList(): List<Product> {
        return mockProductsResponse().products.map { it.toProduct() }
    }

    fun mockFavouriteProductsList(): List<Product> {
        val sublist = mockProductsList().subList(0, 3)
        sublist.map { it.isFavourite = true }
        return sublist
    }
}
