package com.example.googlepaylive

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.checkout.components.core.CheckoutComponentsFactory
import com.checkout.components.interfaces.Environment
import com.checkout.components.interfaces.api.CheckoutComponents
import com.checkout.components.interfaces.api.PaymentMethodComponent
import com.checkout.components.interfaces.component.CheckoutComponentConfiguration
import com.checkout.components.interfaces.component.ComponentCallback
import com.checkout.components.interfaces.component.FlowCoordinator
import com.checkout.components.interfaces.error.CheckoutError
import com.checkout.components.interfaces.model.ComponentName
import com.checkout.components.interfaces.model.PaymentMethodName
import com.checkout.components.interfaces.model.PaymentSessionResponse
import com.checkout.components.sampleapp.BuildConfig
import com.checkout.components.wallet.wrapper.GooglePayFlowCoordinator
import com.example.googlepaylive.screen.PaymentSessionDelegate
import com.example.googlepaylive.screen.PaymentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class SampleViewModel @Inject constructor(
    private val paymentSessionDelegate: PaymentSessionDelegate,
) : ViewModel() {
    private val _paymentSessionState = MutableStateFlow(PaymentUiState())
    val paymentSessionState: StateFlow<PaymentUiState> = _paymentSessionState

    // STEP 6-1 Store component and isAvailable as StateFlow
    private val _component: MutableStateFlow<PaymentMethodComponent?> = MutableStateFlow(null)
    val component: StateFlow<PaymentMethodComponent?> = _component
    private val _isAvailable = MutableStateFlow(false)
    val isAvailable: StateFlow<Boolean> = _isAvailable

    // STEP 7-1 Create a GooglePayFlowCoordinator
    private val checkoutComponents: MutableStateFlow<CheckoutComponents?> = MutableStateFlow(null)
    private val googlePayFlowCoordinator = MutableStateFlow<FlowCoordinator?>(null)

    // STEP 7-6 Setting the FlowCoordinator
    fun setFlowCoordinator(wrapper: GooglePayFlowCoordinator) {
        googlePayFlowCoordinator.value = wrapper
    }
    // STEP 7-7 Implement the handleActivityResult
    fun handleActivityResult(
        resultCode: Int,
        data: String,
    ) {
        checkoutComponents.value?.handleActivityResult(resultCode, data)
    }

    private suspend fun createPaymentSession() {
        _paymentSessionState.update { it.copy(isLoading = true, error = null) }
        val paymentSession = paymentSessionDelegate.createPaymentSession(
            paymentMethodSupported = listOf("card", "googlepay"),
            currentState = _paymentSessionState.value,
        )
        _paymentSessionState.update { paymentSession }
    }

    fun renderFlow(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            // STEP 1 Create the payment session
            createPaymentSession()
            // STEP 2-1 Create the function for the CheckoutComponent
            withContext(Dispatchers.Main) {
                createCheckoutComponent(context = context)
            }
        }
    }

    // STEP 2-2 Create the createCheckoutComponent
    private fun createCheckoutComponent(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            // STEP 3 Create the configuration needed for the factory
            val configuration = CheckoutComponentConfiguration(
                context = context,
                paymentSession = PaymentSessionResponse(
                    id = _paymentSessionState.value.paymentSessionResponse.id,
                    paymentSessionToken = _paymentSessionState.value.paymentSessionResponse.paymentSessionToken,
                    paymentSessionSecret = _paymentSessionState.value.paymentSessionResponse.paymentSessionSecret,
                ),
                publicKey = BuildConfig.SANDBOX_PUBLIC_KEY,
                environment = Environment.SANDBOX,
                // STEP 7-2 Add the flowCoordinator as map
                flowCoordinators = googlePayFlowCoordinator.value?.let {
                    mapOf(PaymentMethodName.GooglePay to it)
                } ?: emptyMap(),
                componentCallback = ComponentCallback(
                    onReady = { component ->
                        println("onReady: ${component.name}")
                    },
                    onSubmit = { component ->
                        println("onSubmit: ${component.name}")
                    },
                    onSuccess = { component, paymentId ->
                        println("onSuccess: ${component.name} - $paymentId")
                    },
                    onError = { component, checkoutError ->
                        println("onError ${component.name}: $checkoutError")
                    },
                )
            )
            // STEP 4 Create the component factory
            try {
                val checkoutComponents = CheckoutComponentsFactory(config = configuration).create()
                // STEP 5-1 Create the component to handle all the payment option
                val flowComponent = checkoutComponents.create(ComponentName.Flow)
                // STEP 5-2 or one of the option below for the single payment method
//                val flowComponent = checkoutComponents.create(PaymentMethodName.Card)
//                val flowComponent = checkoutComponents.create(PaymentMethodName.GooglePay)

                // STEP 6-2 Update component and isAvailable
                _component.update { flowComponent }
                _isAvailable.update { flowComponent.isAvailable() }
                // STEP 7-3 Update the factory in the view model
                this@SampleViewModel.checkoutComponents.update { checkoutComponents }


            } catch (checkoutError: CheckoutError) {
                _paymentSessionState.update { it.copy(error = checkoutError.toString()) }
            } finally {
                _paymentSessionState.update { it.copy(isLoading = false) }
            }
        }

    }
}
