package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class Telacadastro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.telacadastro)

        val primeiroNome = findViewById<EditText>(R.id.primeiroNomeTexto)
        val ultimoNome = findViewById<EditText>(R.id.ultimoNomeTexto)
        val emailTexto = findViewById<EditText>(R.id.emailTexto)
        val senhaTexto = findViewById<EditText>(R.id.senhaTexto)
        val idEstudante = findViewById<EditText>(R.id.idEstudanteTexto)
        val voltarc = findViewById<ImageButton>(R.id.voltarCadastro)
        val confirmarRegistro = findViewById<ImageView>(R.id.registroBotao)

        voltarc.setOnClickListener {
            val intent = Intent(this, Telalogin::class.java)
            startActivity(intent)
        }

        confirmarRegistro.setOnClickListener {
            if (verificarCamposPreenchidos(primeiroNome, ultimoNome, emailTexto, senhaTexto, idEstudante)) {
                val jsonBody = org.json.JSONObject().apply {
                    put("nome", primeiroNome.text.toString() + " " + ultimoNome.text.toString())
                    put("email", emailTexto.text.toString())
                    put("senha", senhaTexto.text.toString())
                    put("idEstudante", idEstudante.text.toString())
                }
                var respostaApi: String? = null
                ApiClient.request(
                    url = "https://d2gmlnphe8ordg.cloudfront.net/api/notesync/auth/registro",
                    method = "POST",
                    jsonBody = jsonBody
                ) { success, response ->
                    respostaApi = response
                    runOnUiThread {
                        if (success) {
                            Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Erro: $response", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "não está preenchido", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun verificarCamposPreenchidos(
        primeiroNome: EditText,
        ultimoNome: EditText,
        emailTexto: EditText,
        senhaTexto: EditText,
        idEstudante: EditText
    ): Boolean {
        return primeiroNome.text.toString().isNotBlank() &&
                ultimoNome.text.toString().isNotBlank() &&
                emailTexto.text.toString().isNotBlank() &&
                senhaTexto.text.toString().isNotBlank() &&
                idEstudante.text.toString().isNotBlank()
    }

}

