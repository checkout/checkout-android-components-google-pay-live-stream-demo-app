package com.example.googlepaylive.models

import com.squareup.moshi.Json

data class PaymentContextItem(
    private val name: String,
    @Json(name = "unit_price")
    private val unitPrice: Int,
    private val quantity: Int,
)
