package com.google.mapi.business

import android.content.Context
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.inject.Inject

class TakeoutSavedCollectionsService @Inject constructor() {
    private val client = OkHttpClient()

    fun getTakeoutData(ctx: Context, accessToken: String, successfulCsvSaving: (Boolean) -> Unit) {
        createRequest(accessToken = accessToken) { jobId ->

            if (jobId != null) {
                var isFinished = false
                var url: String? = null
                while (!isFinished) {
                    poll(jobId = jobId, accessToken) { isCompleted, data ->
                        if (isCompleted) {
                            isFinished = true
                            if (data != null) {
                                url = data.getString(0)
                            }
                        } else {
                            Log.d("PullAndWaitForData", "Data not ready yet")
                        }
                    }
                    Thread.sleep(3000)
                }
                if (url != null) {
                    downloadZipAndParse(ctx, url!!, successfulCsvSaving)
                }
            }
        }
    }

    private fun createRequest(accessToken: String, callback: (String?) -> Unit) {
        Log.d("PullAndWaitForData", "Initiating data pull")
        val request = Request.Builder()
            .url("https://dataportability.googleapis.com/v1/portabilityArchive:initiate")
            .header("Authorization", "Bearer $accessToken")
            .post(RequestBody.create(null, "{\"resources\": [\"saved.collections\"]}"))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let {
                        val jsonObject = JSONObject(it)
                        val archiveJobId = jsonObject.getString("archiveJobId")
                        callback(archiveJobId)
                    }
                } else {
                    Log.d(
                        "PullAndWaitForData",
                        "Failed to initiate data pull+${response.body?.string()}"
                    )
                    callback(null)
                }
            }
        })
    }

    private fun poll(jobId: String, accessToken: String, callback: (Boolean, JSONArray?) -> Unit) {
        val request = Request.Builder()
            .url("https://dataportability.googleapis.com/v1/archiveJobs/$jobId/portabilityArchiveState?alt=json")
            .header("Authorization", "Bearer $accessToken")
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("PullAndWaitForData", "Failed to poll data pull+${e.message}")
                callback(true, null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let {
                        val jsonObject = JSONObject(it)
                        val state = jsonObject.getString("state")
                        val urls = jsonObject.getJSONArray("urls")
                        Log.d("PullAndWaitForData", "Polling data pull state $state")
                        when (state) {
                            "FAILED" -> {
                                callback(true, null)
                            }

                            "COMPLETE" -> {
                                callback(true, urls)
                            }

                            else -> {
                                callback(false, null)
                            }
                        }
                    }
                } else {
                    Log.d(
                        "PullAndWaitForData",
                        "Failed to poll data pull+${response.body?.string()}"
                    )
                    callback(true, null)
                }
            }
        })
    }

    private fun downloadZipAndParse(
        ctx: Context,
        url: String,
        successfulCsvSaving: (Boolean) -> Unit
    ) {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        client.newCall(request)
            .enqueue(downloadZipAndParseTakeoutCallback(ctx, successfulCsvSaving))
    }

    private fun downloadZipAndParseTakeoutCallback(
        ctx: Context,
        successfulCsvSaving: (Boolean) -> Unit
    ) = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("PullAndWaitForData", "Failed to download zip file+${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                if (response.body?.byteStream() != null) {
                    val byteStream = response.body?.byteStream()
                    val zis = ZipInputStream(byteStream)
                    val csv = StringBuilder()
                    val buffer = ByteArray(1024)
                    var entry: ZipEntry? = zis.nextEntry
                    while (entry != null) {
                        if (entry.name.endsWith(".csv")) {
                            while (zis.read(buffer) > 0) {
                                csv.append(String(buffer))
                            }
                        }
                        zis.closeEntry()
                        entry = zis.nextEntry
                    }
                    ctx.openFileOutput("latest.csv", Context.MODE_PRIVATE).use {
                        it.write(csv.toString().toByteArray())
                        successfulCsvSaving(true)
                        Log.d("PullAndWaitForData", "CSV file saved")
                    }
                } else {
                    successfulCsvSaving(false)
                }
            } else {
                Log.d(
                    "PullAndWaitForData",
                    "Failed to download zip file+${response.body?.string()}"
                )
                successfulCsvSaving(true)
            }
        }
    }
}