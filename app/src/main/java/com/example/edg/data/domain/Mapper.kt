package com.example.edg.data.domain

import com.example.edg.data.domain.model.Price
import com.example.edg.data.domain.model.Product
import com.example.edg.data.network.model.PriceDto
import com.example.edg.data.network.model.ProductDto

fun ProductDto.toProduct(): Product {
    return Product(
        brand = brand,
        id = id,
        imageURL = imageURL,
        price = price.map { it.toPrice() },
        ratingCount = ratingCount,
        title = title,
        isFavourite = false
    )
}

fun PriceDto.toPrice(): Price {
    return Price(
        message = message,
        value = value
    )
}
