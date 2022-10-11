package com.example.eda.network

import com.example.eda.network.model.ProductsResponse
import retrofit2.http.GET

interface ProductsService {

    @GET("/v3/2f06b453-8375-43cf-861a-06e95a951328")
    suspend fun getProducts(): ProductsResponse
}
