package com.example.googlepaylive.models

import com.squareup.moshi.Json

internal data class Address(
    val country: String,
    @Json(name = "address_line1")
    val addressLine1: String? = null,
    @Json(name = "address_line2")
    val addressLine2: String? = null,
    val city: String? = null,
    val zip: String? = null,
    val state: String? = null,
)
