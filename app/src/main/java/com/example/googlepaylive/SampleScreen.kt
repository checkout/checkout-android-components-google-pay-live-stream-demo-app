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
import com.example.googlepaylive.screen.PaymentUiState

@Composable
internal fun SampleScreen(viewModel: SampleViewModel = hiltViewModel<SampleViewModel>()) {
    val context = LocalContext.current
    val uiState = viewModel.paymentSessionState.collectAsState()
    SampleScreen(
        { viewModel.renderFlow(context) },
        uiState,
    )
}

@Composable
internal fun SampleScreen(
    onShowPaymentComponentButtonClick: () -> Unit,
    uiState: State<PaymentUiState>,
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
            Button(onClick = onShowPaymentComponentButtonClick) {
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
        }
    }
}
