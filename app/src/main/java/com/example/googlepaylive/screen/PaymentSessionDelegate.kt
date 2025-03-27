package com.example.googlepaylive.screen

import com.checkout.components.interfaces.model.PaymentSessionResponse
import com.example.googlepaylive.network.CreatePaymentSessionsResponse
import com.example.googlepaylive.network.PaymentSessionRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
internal class PaymentSessionDelegate
    @Inject
    constructor(
        private val repository: PaymentSessionRepository,
    ) {
        suspend fun createPaymentSession(
            paymentMethodSupported: List<String>,
            currentState: PaymentUiState,
        ): PaymentUiState =
            when (
                val response =
                    repository.createPaymentSession(paymentMethodSupported = paymentMethodSupported)
            ) {
                is CreatePaymentSessionsResponse.Success ->
                    currentState.copy(
                        isLoading = false,
                        message = "Payment session token received",
                        paymentSessionResponse =
                            with(response) {
                                PaymentSessionResponse(
                                    id = id,
                                    paymentSessionToken = paymentSessionToken,
                                    paymentSessionSecret = paymentSessionSecret,
                                )
                            },
                    )

                is CreatePaymentSessionsResponse.Failure ->
                    currentState.copy(
                        isLoading = false,
                        error = "Error codes: ${response.errorCodes.joinToString()}",
                    )

                is CreatePaymentSessionsResponse.Unknown ->
                    currentState.copy(
                        isLoading = false,
                        error = "Unknown response: ${response.message}",
                    )

                CreatePaymentSessionsResponse.Unauthorized ->
                    currentState.copy(
                        isLoading = false,
                        error = "Authentication Failed, please check your secret key",
                    )
            }
    }
