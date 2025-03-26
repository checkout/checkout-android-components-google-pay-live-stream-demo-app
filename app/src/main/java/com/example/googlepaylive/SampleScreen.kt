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
    // Step 6-2  adding the component and isAvailable to the SampleScreen
    val checkoutComponent = viewModel.component.collectAsState()
    val isAvailable = viewModel.isAvailable.collectAsState()

    val context = LocalContext.current
    val uiState = viewModel.paymentSessionState.collectAsState()
    SampleScreen(
        { viewModel.renderFlow(context) },
        uiState,
        // Step 6-3
        checkoutComponent,
        isAvailable,
    )
}

@Composable
internal fun SampleScreen(
    onCreatePaymentSessionButtonClick: () -> Unit,
    uiState: State<PaymentUiState>,
    // Step 6-4
    component: State<PaymentMethodComponent?>,
    isAvailable: State<Boolean>,
) {
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
                Text(text = "Show Payment Component")
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
            // Step 6-5
            if (isAvailable.value) {
                component.value?.Render()
            }
        }
    }
}
