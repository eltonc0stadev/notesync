package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class Telaperfil : AppCompatActivity() {

    private lateinit var userId: TextView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userStudentId: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.perfil)

        // Inicializa as views
        userId = findViewById(R.id.userId)
        userName = findViewById(R.id.userName)
        userEmail = findViewById(R.id.userEmail)
        userStudentId = findViewById(R.id.userStudentId)

        // Configura o botão de voltar
        findViewById<ImageButton>(R.id.voltarperfil).setOnClickListener {
            onBackPressed()
        }

        // Configura o botão de logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            // Limpa o token
            getSharedPreferences("notesync_prefs", MODE_PRIVATE)
                .edit()
                .remove("auth_token")
                .apply()

            // Redireciona para a tela de login
            val intent = Intent(this, Telalogin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Carrega as informações do usuário
        carregarInformacoesUsuario()
    }

    private fun carregarInformacoesUsuario() {
        val token = getSharedPreferences("notesync_prefs", MODE_PRIVATE)
            .getString("auth_token", null)

        if (token == null) {
            Toast.makeText(this, "Token não encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        ApiClient.request(
            url = "https://d2gmlnphe8ordg.cloudfront.net/api/notesync/usuario/info",
            method = "GET",
            headers = mapOf("Authorization" to "Bearer $token")
        ) { success, response ->
            runOnUiThread {
                if (success && response != null) {
                    try {
                        val userInfo = JSONObject(response)
                        userId.text = userInfo.optString("id", "Não disponível")
                        userName.text = userInfo.optString("nome", "Não disponível")
                        userEmail.text = userInfo.optString("email", "Não disponível")
                        userStudentId.text = userInfo.optString("idEstudante", "Não disponível")
                    } catch (e: Exception) {
                        Toast.makeText(this, "Erro ao processar dados do usuário", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Erro ao carregar informações do usuário", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}