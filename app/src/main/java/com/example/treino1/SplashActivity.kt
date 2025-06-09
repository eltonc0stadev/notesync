package com.example.treino1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validarToken()
    }

    private fun validarToken() {
        val prefs = getSharedPreferences("notesync_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        if (token.isNullOrBlank()) {
            irParaLogin()
            return
        }
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://d2gmlnphe8ordg.cloudfront.net/api/notesync/auth/validar-token")
            .addHeader("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    irParaLogin()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (response.isSuccessful && body == "true") {
                    runOnUiThread { irParaPrincipal() }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@SplashActivity, "Faça o login novamente", Toast.LENGTH_LONG).show()
                        irParaLogin()
                    }
                }
            }
        })
    }

    private fun irParaPrincipal() {
        val intent = Intent(this, Telaprincipal::class.java)
        startActivity(intent)
        finish()
    }

    private fun irParaLogin() {
        val intent = Intent(this, Telalogin::class.java)
        startActivity(intent)
        finish()
    }
}

