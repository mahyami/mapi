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

class PullAndWaitForData constructor(private val accessToken: String) {
    private val client = OkHttpClient()


    fun init(callback: (String?) -> Unit) {
        Log.d("PullAndWaitForData", "Initiating data pull")
        val request = Request.Builder()
            .url("https://dataportability.googleapis.com/v1/portabilityArchive:initiate")
            .header("Authorization", "Bearer "+ this.accessToken)
            .post(RequestBody.create(null, "{\"resources\": [\"saved.collections\"]}"))
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
                        val accessToken = jsonObject.getString("archiveJobId")
                        callback(accessToken)
                    }
                } else {
                    // Handle unsuccessful response
                    Log.d("PullAndWaitForData", "Failed to initiate data pull+${response.body?.string()}")
                    callback(null)
                }
            }
        })
    }

    private fun poll(jobId: String, callback: (Boolean, JSONArray?) -> Unit) {
        val request = Request.Builder()
            .url("https://dataportability.googleapis.com/v1/archiveJobs/$jobId/portabilityArchiveState?alt=json")
            .header("Authorization", "Bearer "+ this.accessToken)
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle request failure
                Log.d("PullAndWaitForData", "Failed to poll data pull+${e.message}")
                callback(true, null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let {
                        // Parse the response
                        val jsonObject = JSONObject(it)
                        val state = jsonObject.getString("state")
                        val urls = jsonObject.getJSONArray("urls")
                        Log.d("PullAndWaitForData", "Polling data pull state $state")
                        when (state) {
                            "FAILED" -> {
                                // Handle failure
                                callback(true, null)
                            }
                            "COMPLETE" -> {
                                callback(true, urls)
                            }
                            else -> {
                                // Not finished
                                callback(false, null)
                            }
                        }
                    }
                } else {
                    // Handle unsuccessful response
                    Log.d("PullAndWaitForData", "Failed to poll data pull+${response.body?.string()}")
                    callback(true, null)
                }
            }
        })
    }

    fun getDataUrl(ctx: Context){
        init() { jobId ->
            Log.d("PullAndWaitForData", "Data pull initiated with jobid $jobId")

            if (jobId != null) {
                // Start polling
                var isFinished = false
                var url: String? = null
                while (!isFinished) {
                    poll(jobId) { isCompleted, data ->
                        // Handle the data
                        if (isCompleted) {
                            // Data is ready
                            isFinished = true
                            if(data != null) {
                                url = data.getString(0)
                            }
                        } else {
                            // Data is not ready
                            Log.d("PullAndWaitForData", "Data not ready yet")
                        }
                    }
                    Thread.sleep(3000)
                }
                if(url != null) {
                    downloadZipAndParse(ctx, url!!)
                }
            }
        }
    }

    fun downloadZipAndParse(ctx: Context, url: String) {
        // Download the zip file
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle request failure
                Log.d("PullAndWaitForData", "Failed to download zip file+${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.byteStream()?.let {
                        // Parse the zip file
                        val zis = ZipInputStream(it)
                        val csv = StringBuilder()
                        var buffer = ByteArray(1024)
                        var bytesRead: Int
                        var entry: ZipEntry? = zis.nextEntry
                        while (entry != null) {
                            if(entry.name.endsWith(".csv")) {
                                // Parse the CSV file
                                while (zis.read(buffer) > 0) {
                                    csv.append(String(buffer))
                                }
                            }
                            // Parse the entry
                            zis.closeEntry()
                            entry = zis.nextEntry
                        }
//                      // Store the CSV data in the database
                        ctx.openFileOutput("latest.csv", Context.MODE_PRIVATE).use {
                            it.write(csv.toString().toByteArray())
                            Log.d("PullAndWaitForData", "CSV file saved")
                            // send an event saying the latest.csv updated

                        }
                    }
                } else {
                    // Handle unsuccessful response
                    Log.d("PullAndWaitForData", "Failed to download zip file+${response.body?.string()}")
                }
            }
        })
    }
}