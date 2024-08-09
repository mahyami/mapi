package com.google.mapi.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.mapi.ui.compose.GreetingScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MapiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val uiState by mainViewModel.uiState.collectAsState()
                    GreetingScreen(
                        onSyncButtonClicked = {
                            mainViewModel.onSyncButtonClicked()
                        },
                        onAskGeminiButtonClicked = {
                            mainViewModel.onSubmitButtonClicked(it)
                        },
                        onOpenMapsClicked = {
                            mainViewModel.onOpenMapsClicked(it)
                        },
                        uiState = uiState
                    )
                }
            }
        }

        lifecycleScope.launch {
            mainViewModel.uiEvent.collect { uiEvent ->
                when (uiEvent) {
                    is MainViewModel.UiEvent.OpenBrowser -> openBrowser(uiEvent.url)
                    MainViewModel.UiEvent.Default -> {}
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val uri: Uri? = intent.data
        if (uri != null) {
            mainViewModel.handleGoogleCallback(this, uri)
        }
    }

    private fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}

