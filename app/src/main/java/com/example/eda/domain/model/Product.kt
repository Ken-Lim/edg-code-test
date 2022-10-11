package com.example.eda.domain.model

data class Product(
    val brand: String,
    val id: String,
    val imageURL: String,
    val price: List<Price>,
    val ratingCount: Double,
    val title: String,
    val isFavourite: Boolean
)
