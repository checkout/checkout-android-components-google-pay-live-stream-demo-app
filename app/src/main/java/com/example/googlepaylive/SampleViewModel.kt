package com.example.googlepaylive

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlepaylive.screen.PaymentSessionDelegate
import com.example.googlepaylive.screen.PaymentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SampleViewModel
    @Inject
    constructor(
        private val paymentSessionDelegate: PaymentSessionDelegate,
    ) : ViewModel() {
        private val _paymentSessionState = MutableStateFlow(PaymentUiState())
        val paymentSessionState: StateFlow<PaymentUiState> = _paymentSessionState

        fun renderFlow(context: Context) {
            viewModelScope.launch(Dispatchers.IO) {
                createPaymentSession()
            }
        }

        private suspend fun createPaymentSession() {
            _paymentSessionState.update { it.copy(isLoading = true, error = null) }
            val paymentSession =
                paymentSessionDelegate.createPaymentSession(
                    paymentMethodSupported = listOf("card", "googlepay"),
                    currentState = _paymentSessionState.value,
                )
            _paymentSessionState.update { paymentSession }
        }
    }
