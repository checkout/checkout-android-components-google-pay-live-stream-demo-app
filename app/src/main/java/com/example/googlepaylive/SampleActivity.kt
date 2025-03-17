package com.example.googlepaylive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.checkout.components.wallet.wrapper.GooglePayFlowCoordinator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : ComponentActivity() {
    // Step 7-3
    private val viewModel: SampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 7-4 add this here THEN implement the missing methods in VM
        val googlePayFlowCoordinator = GooglePayFlowCoordinator(
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
