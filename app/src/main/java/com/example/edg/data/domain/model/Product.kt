package com.example.edg.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val brand: String,
    val id: String,
    val imageURL: String,
    val price: List<Price>,
    val ratingCount: Double,
    val title: String,
    var isFavourite: Boolean
) : Parcelable
