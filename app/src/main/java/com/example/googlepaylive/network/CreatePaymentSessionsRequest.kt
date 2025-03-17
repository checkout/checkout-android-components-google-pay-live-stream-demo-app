package com.example.googlepaylive.network

import com.example.googlepaylive.models.AddressAndPhoneNumber
import com.example.googlepaylive.models.Currency
import com.example.googlepaylive.models.Customer
import com.example.googlepaylive.models.PaymentContextItem
import com.example.googlepaylive.models.PaymentType
import com.example.googlepaylive.models.Threeds
import com.squareup.moshi.Json

internal data class CreatePaymentSessionsRequest(
    val amount: Long,
    val currency: Currency,
    val reference: String,
    val billing: AddressAndPhoneNumber,
    val customer: Customer,
    @Json(name = "processing_channel_id")
    val processingChannelID: String? = null,
    @Json(name = "success_url")
    val successUrl: String,
    @Json(name = "failure_url")
    val failureUrl: String,
    @Json(name = "3ds")
    val threeds: Threeds,
    @Json(name = "enabled_payment_methods")
    val enabledPaymentMethods: List<String> = emptyList(),
    val items: List<PaymentContextItem>? = null,
    val description: String? = null,
    val shipping: AddressAndPhoneNumber? = null,
    @Json(name = "payment_type")
    val paymentType: PaymentType? = null,
    /**
     * eg. "en-GB", "ar"
     * more info at https://api-reference.checkout.com/#operation/CreatePaymentSession!path=locale&t=request
     */
    val locale: String? = null,
)
