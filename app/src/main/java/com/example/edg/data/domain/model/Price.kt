package com.example.edg.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(
    val message: String,
    val value: Double
) : Parcelable
