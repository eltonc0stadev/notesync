package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import android.content.Context

class Telalogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.telalogin)

        val botaoConfirmar = findViewById<TextView>(R.id.cadastrobuttom)
        botaoConfirmar.setOnClickListener {
            val intent = Intent(this, Telacadastro::class.java)
            startActivity(intent)
        }
        val botaoConectar = findViewById<ImageView>(R.id.conectar)
        val emailEditText = findViewById<EditText>(R.id.emailogin)
        val senhaEditText = findViewById<EditText>(R.id.senhalogin)

        botaoConectar.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()
            val jsonBody = org.json.JSONObject().apply {
                put("email", email)
                put("senha", senha)
            }
            ApiClient.request(
                url = "https://d2gmlnphe8ordg.cloudfront.net/api/notesync/auth/login",
                method = "POST",
                jsonBody = jsonBody
            ) { success, response ->
                runOnUiThread {
                    if (success && response != null) {
                        try {
                            val json = org.json.JSONObject(response)
                            val token = json.optString("token", null)
                            if (token != null) {
                                val prefs = getSharedPreferences("notesync_prefs", Context.MODE_PRIVATE)
                                prefs.edit().putString("auth_token", token).apply()
                                Toast.makeText(this, "Login feito com sucesso", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, Telaprincipal::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Algo deu errado, tente novamente", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this, "Algo deu errado, tente novamente", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Algo deu errado, tente novamente", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}