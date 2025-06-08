package com.example.treino1

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

object ApiClient {
    private val client = OkHttpClient()

    fun request(
        url: String,
        method: String = "GET",
        jsonBody: JSONObject? = null,
        headers: Map<String, String> = emptyMap(),
        callback: (success: Boolean, response: String?) -> Unit
    ) {
        val builder = Request.Builder().url(url)
        for ((key, value) in headers) {
            builder.addHeader(key, value)
        }
        when (method.uppercase()) {
            "POST", "PUT" -> {
                val body = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(),
                    jsonBody?.toString() ?: ""
                )
                if (method.uppercase() == "POST") builder.post(body)
                else builder.put(body)
            }
            "DELETE" -> builder.delete()
            else -> builder.get()
        }
        val request = builder.build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message)
            }
            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful, response.body?.string())
            }
        })
    }
}

