package com.google.mapi.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.mapi.ui.compose.GreetingScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.getPlacesDetails(this)

        setContent {
            MapiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    GreetingScreen(
                        onSyncButtonClicked = {
                            onSyncButtonClick()
                        },
                        onSubmitButtonClicked = {
                            mainViewModel.onSubmitButtonClicked(it)
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val uri: Uri? = intent.data
        Log.d("OATH:: ", uri.toString())
        if (uri != null) {
            Log.d("OATH:: ", uri.toString())
            mainViewModel.handleGoogleCallback(this, uri)
        }
    }


    private fun onSyncButtonClick() {
        val redirectUri = "https://ipiyush.com/mapi/"
        val takoutApi = "https://www.googleapis.com/auth/dataportability.saved.collections"
        val url = "https://accounts.google.com/o/oauth2/auth?response_type=code" +
                "&client_id=1007629705241-20m5rskcp6iqlrrfthrhs5h05pur5oan.apps.googleusercontent.com" +
                "&redirect_uri=$redirectUri" +
                "&scope=$takoutApi" +
                "&state=eJmhKiMS3CX0bbO1aTwaTzM07cULhG" +
                "&access_type=offline"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }


}

