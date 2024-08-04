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
import com.google.mapi.business.PullAndWaitForData
import com.google.mapi.ui.compose.GreetingScreen
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
    private val client = OkHttpClient()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.getPlacesDetails(this)

        setContent {
            MapiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    GreetingScreen { onSyncButtonClick() }
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
            handleCallback(uri)
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

    private fun exchangeCodeForToken(
        code: String,
        callback: (String?) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("code", code)
            .add(
                "client_id",
                "1007629705241-20m5rskcp6iqlrrfthrhs5h05pur5oan.apps.googleusercontent.com"
            )
            .add("client_secret", "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")
            .add("redirect_uri", "https://ipiyush.com/mapi/")
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
                    Log.d("MainActivity", "Failed to exchange code for token+${response.body?.string()}")
                    callback(null)
                }
            }
        })
    }
}

