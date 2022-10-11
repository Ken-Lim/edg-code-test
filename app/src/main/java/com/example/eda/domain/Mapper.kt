package com.example.eda.domain

import com.example.eda.domain.model.Price
import com.example.eda.domain.model.Product
import com.example.eda.network.model.PriceDto
import com.example.eda.network.model.ProductDto

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
