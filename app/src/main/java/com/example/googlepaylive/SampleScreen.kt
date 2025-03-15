package com.example.googlepaylive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.checkout.components.interfaces.api.PaymentMethodComponent
import com.example.googlepaylive.screen.PaymentUiState

@Composable
internal fun SampleScreen(viewModel: SampleViewModel = hiltViewModel<SampleViewModel>()) {
    // Step 8 adding the component to the SampleScreen
    val checkoutComponent = viewModel.component.collectAsState()
    val isAvailable = viewModel.isAvailable.collectAsState()

    val context = LocalContext.current
    val uiState = viewModel.paymentSessionState.collectAsState()
    SampleScreen(
        // Step 1-2
//        viewModel::createPaymentSession,
        { viewModel.renderFlow(context) },
        uiState,
        // Step 8-2
        checkoutComponent,
        isAvailable,

        )
}

@Composable
internal fun SampleScreen(
    onCreatePaymentSessionButtonClick: () -> Unit,
    uiState: State<PaymentUiState>,
    // Step 8-3
    component: State<PaymentMethodComponent?>,
    isAvailable: State<Boolean>
) {
    // Step 8-4
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = onCreatePaymentSessionButtonClick) {
                Text(text = "Create Payment Session")
            }
            if (uiState.value.isLoading) {
                Text("Loading...")
            }
            uiState.value.error?.let {
                Text(
                    text = "Error: $it",
                    textAlign = TextAlign.Center,
                )
            }
            // Step 8-6
            if (isAvailable.value) {
                component.value?.Render()
            }
        }
    }
}
