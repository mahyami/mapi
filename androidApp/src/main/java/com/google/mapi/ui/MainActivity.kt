package com.google.mapi.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.mapi.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var myWebView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.getPlacesDetails(this)

        setContent {
            MapiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    GreetingView({})
                }
            }
        }

        // Redirecting to FirebaseUIActivity immediately.
        // Add conditions here if needed.
        if (OAUTH_FEATURE_FLAG) {
            startActivity(
                Intent(
                    this@MainActivity,
                    FirebaseUIActivity::class.java
                )
            )
        }
        // Consider setting a view or using setContent if this activity should display content.

        if (WEBVIEW_OAUTH_FEATURE_FLAG) {
            setContentView(R.layout.webview);
            myWebView = findViewById(R.id.webView)
            myWebView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                userAgentString =
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36"
            }
            myWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url!!.startsWith("https://mapicallbackdomain.com/callback/")) {
                        // Handle the URL
                        handleCallback(Uri.parse(url))
                        myWebView.visibility = View.GONE
                        myWebView.destroy()
                        setContent {
                            MapiTheme {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    GreetingView({})
                                }
                            }
                        }
                        return true
                    }
                    return false
                }
            }
            // Clear cache to avoid caching issues
            myWebView.clearCache(true)

            // Load a URL
            myWebView.loadUrl("https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=1007629705241-20m5rskcp6iqlrrfthrhs5h05pur5oan.apps.googleusercontent.com&redirect_uri=https://mapicallbackdomain.com/callback/&scope=https://www.googleapis.com/auth/dataportability.saved.collections&state=eJmhKiMS3CX0bbO1aTwaTzM07cULhG&access_type=offline")
        }
    }

    private fun handleCallback(uri: Uri) {
        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")
        Log.d("MainActivity", "Code: $code, State: $state")
    }

    companion object {
        private const val OAUTH_FEATURE_FLAG = false
        private const val WEBVIEW_OAUTH_FEATURE_FLAG = false
    }
}

@Composable
fun GreetingView(onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title_welcome),
            fontWeight = FontWeight.Bold,
            color = Colors.Primary,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.subtitle_welcome),
            color = Colors.Primary,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.footer_welcome),
            color = Colors.Primary,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            onClick = onButtonClick,

        ) {
            Text(text = stringResource(R.string.button_sync))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DefaultPreview() {
    MapiTheme {
        GreetingView({})
    }
}
