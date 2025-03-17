package com.example.googlepaylive.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

internal sealed class CreatePaymentSessionsResponse {
    @JsonClass(generateAdapter = true)
    data class Success(
        val id: String,
        val links: Links?,
        @Json(name = "payment_session_token")
        val paymentSessionToken: String,
        @Json(name = "payment_session_secret")
        val paymentSessionSecret: String,
    ) : CreatePaymentSessionsResponse() {
        data class Links(val self: Self)

        data class Self(val href: String)
    }

    @JsonClass(generateAdapter = true)
    data class Failure(
        @Json(name = "request_id") val requestID: String,
        @Json(name = "error_type") val errorType: String,
        @Json(name = "error_codes") val errorCodes: List<String>,
    ) : CreatePaymentSessionsResponse()

    @JsonClass(generateAdapter = true)
    data class Unknown(val message: String) : CreatePaymentSessionsResponse()

    data object Unauthorized : CreatePaymentSessionsResponse()
}
