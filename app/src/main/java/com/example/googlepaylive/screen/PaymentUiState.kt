package com.example.googlepaylive.screen

import com.checkout.components.interfaces.model.PaymentSessionResponse

data class PaymentUiState(
    val paymentSessionResponse: PaymentSessionResponse = PaymentSessionResponse("", "", ""),
    val message: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)
