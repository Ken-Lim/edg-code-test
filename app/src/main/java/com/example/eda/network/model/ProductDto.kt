package com.example.eda.network.model

data class ProductDto(
    val brand: String,
    val id: String,
    val imageURL: String,
    val price: List<PriceDto>,
    val ratingCount: Double,
    val title: String
)
