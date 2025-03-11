package com.example.googlepaylive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.checkout.components.wallet.wrapper.GooglePayFlowCoordinator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : ComponentActivity() {
    // Step 9 wallet is not inflating beacuse is expecting the coordinator
    private val viewModel: SampleViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 9-1 add this here THEN move to the VM
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
