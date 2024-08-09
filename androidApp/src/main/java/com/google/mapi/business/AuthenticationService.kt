package com.google.mapi.business

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class AuthenticationService @Inject constructor(
    private val pullAndWaitForDataService: PullAndWaitForDataService
) {

    fun handleCallback(context: Context, uri: Uri) {
        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")
        Log.d("MainActivity", "Code: $code, State: $state")
        exchangeCodeForToken(code!!) { accessToken ->
            pullAndWaitForDataService.getDataUrl(context, requireNotNull(accessToken))
        }
    }

    private fun exchangeCodeForToken(
        code: String,
        callback: (String?) -> Unit
    ) {
        val client = OkHttpClient()

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
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let {
                        val jsonObject = JSONObject(it)
                        val accessToken = jsonObject.getString("access_token")
                        callback(accessToken)
                    }
                } else {
                    callback(null)
                }
            }
        })
    }
}