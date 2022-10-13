package com.example.edg.data.network

import com.example.edg.data.network.model.ProductsResponse
import retrofit2.http.GET

interface ProductService {

    @GET("/v3/2f06b453-8375-43cf-861a-06e95a951328")
    suspend fun getProducts(): ProductsResponse
}
