package com.example.googlepaylive.models

internal data class Card
    @JvmOverloads
    constructor(
        val expiryDate: ExpiryDate,
        val number: String,
        val name: String? = null,
        val cvv: String? = null,
        val billingAddress: Address? = null,
        val phone: Phone? = null,
    )
