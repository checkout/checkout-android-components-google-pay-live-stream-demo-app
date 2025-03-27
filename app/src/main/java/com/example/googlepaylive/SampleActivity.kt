package com.example.googlepaylive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.checkout.components.wallet.wrapper.GooglePayFlowCoordinator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : ComponentActivity() {
    // STEP 7-4
    private val viewModel: SampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 7-5 Create the flow coordinator here, then implement the missing methods in ViewModel
        val googlePayFlowCoordinator =
            GooglePayFlowCoordinator(
                context = this,
                handleActivityResult = { resultCode, data ->
                    viewModel.handleActivityResult(resultCode, data)
                },
            )

        viewModel.setFlowCoordinator(googlePayFlowCoordinator)

        setContent {
            SampleScreen()
        }
    }
}
