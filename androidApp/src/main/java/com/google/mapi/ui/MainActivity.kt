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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.mapi.android.R
import com.google.mapi.business.PullAndWaitForData
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var myWebView: WebView
    private val client = OkHttpClient()
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.getPlacesDetails(this)

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

        if(WEBVIEW_OAUTH_FEATURE_FLAG) {
            setContentView(R.layout.webview);
            myWebView = findViewById(R.id.webView)
            myWebView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36"
            }
            myWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if(url!!.startsWith("https://mapicallbackdomain.com/callback/")) {
                        // Handle the URL
                        handleCallback(Uri.parse(url))
                        myWebView.visibility = View.GONE
                        setContent {
                            MyApplicationTheme {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    GreetingView("Hey android")
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
        exchangeCodeForToken(code!!) { accessToken ->
            // Handle the access token
            Log.d("MainActivity", "Access Token: $accessToken")
            PullAndWaitForData(accessToken!!).getDataUrl(this)
        }
    }
    fun exchangeCodeForToken(
        code: String,
        callback: (String?) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("code", code)
            .add("client_id", "1007629705241-20m5rskcp6iqlrrfthrhs5h05pur5oan.apps.googleusercontent.com")
            .add("client_secret", "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")
            .add("redirect_uri", "https://mapicallbackdomain.com/callback/")
            .add("grant_type", "authorization_code")
            .build()

        val request = Request.Builder()
            .url("https://oauth2.googleapis.com/token")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle request failure
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let {
                        // Parse the response
                        val jsonObject = JSONObject(it)
                        val accessToken = jsonObject.getString("access_token")
                        callback(accessToken)
                    }
                } else {
                    // Handle unsuccessful response
                    callback(null)
                }
            }
        })
    }
    companion object {
        private const val OAUTH_FEATURE_FLAG = false
        private const val WEBVIEW_OAUTH_FEATURE_FLAG = true
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
