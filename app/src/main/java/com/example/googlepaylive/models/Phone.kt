package com.example.googlepaylive.models

import com.squareup.moshi.Json

internal data class Phone(
    val number: String?,
    @Json(name = "country_code")
    val countryCode: String?,
)
