package com.google.mapi.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.mapi.ui.compose.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MapiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val uiState by viewModel.uiState.collectAsState()
                    MainScreen(
                        onSyncButtonClicked = {
                            viewModel.onSyncButtonClicked()
                        },
                        onAskGeminiButtonClicked = {
                            viewModel.onSubmitButtonClicked(it)
                        },
                        onOpenMapsClicked = {
                            viewModel.onOpenMapsClicked(it)
                        },
                        uiState = uiState
                    )
                }
            }
        }

        lifecycleScope.launch {
            viewModel.uiEvent.collect { uiEvent ->
                when (uiEvent) {
                    is MainViewModel.UiEvent.OpenBrowser -> openBrowser(uiEvent.url)
                    MainViewModel.UiEvent.Default -> {}
                    is MainViewModel.UiEvent.ShowToast -> showToast(uiEvent.message)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        val uri: Uri? = intent.data
        if (uri != null) {
            viewModel.onReturnFromOAuth(this, uri)
        }
    }

    private fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}

