package com.example.googlepaylive.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

internal interface PaymentSessionApi {
    @POST("/payment-sessions")
    suspend fun createPaymentSession(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") token: String,
        @Body body: CreatePaymentSessionsRequest,
    ): Response<CreatePaymentSessionsResponse>
}
