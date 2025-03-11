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
import kotlinx.coroutines.CoroutineScope
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

    // Step 6 we need something that holds the checkout components
    private val checkoutComponents: MutableStateFlow<CheckoutComponents?> = MutableStateFlow(null)

    // Step 7 and the components ... to be added in the SampleScreen
    private val _component: MutableStateFlow<PaymentMethodComponent?> = MutableStateFlow(null)
    val component: StateFlow<PaymentMethodComponent?> = _component

    // Step 9-3
    private val googlePayFlowCoordinator = MutableStateFlow<FlowCoordinator?>(null)
    fun setFlowCoordinator(wrapper: GooglePayFlowCoordinator) {
        googlePayFlowCoordinator.value = wrapper
    }

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

    // STEP 1-2  Rewrite a bit the code as the following and call renderFlow() from the SampleScreen
    fun renderFlow(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            createPaymentSession()
            withContext(Dispatchers.Main) {
                createCheckoutComponent(context = context)
            }
        }
    }

    // Step 2 Create the createCheckoutComponent
    private fun createCheckoutComponent(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            // Step 3 from the configuration
            val configuration = CheckoutComponentConfiguration(
                context = context,
                paymentSession = PaymentSessionResponse(
                    id = _paymentSessionState.value.paymentSessionResponse.id,
                    paymentSessionToken = _paymentSessionState.value.paymentSessionResponse.paymentSessionToken,
                    paymentSessionSecret = _paymentSessionState.value.paymentSessionResponse.paymentSessionSecret,
                ),
                publicKey = BuildConfig.SANDBOX_PUBLIC_KEY,
                environment = Environment.SANDBOX,
                // Step 9-4 add the flowCoordinator ... run it an fail since is missing something in the manifest
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
            // Step 4 Create the component factory
            try {
                val checkoutComponents = CheckoutComponentsFactory(config = configuration).create()
                this@SampleViewModel.checkoutComponents.update { checkoutComponents }
                // Step 5 Create the component
                val flowComponent = checkoutComponents.create(ComponentName.Flow)
                // Step 5-2 or one of the option below
//                val flowComponent = checkoutComponents.create(PaymentMethodName.Card)
//                val flowComponent = checkoutComponents.create(PaymentMethodName.GooglePay)
                _component.update { flowComponent }

            } catch (checkoutError: CheckoutError) {
                _paymentSessionState.update { it.copy(error = checkoutError.toString()) }
            } finally {
                _paymentSessionState.update { it.copy(isLoading = false) }
            }
        }

    }
}
