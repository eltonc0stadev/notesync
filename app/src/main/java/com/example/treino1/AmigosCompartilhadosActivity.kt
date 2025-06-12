package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class AmigosCompartilhadosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AmigosAdapter
    private var amigos = arrayListOf<UsuarioCompartilhado>()
    private var idNota: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amigos_compartilhados)

        recyclerView = findViewById(R.id.recyclerViewAmigos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        @Suppress("UNCHECKED_CAST")
        amigos = intent.getSerializableExtra("usuariosCompartilhadosList") as? ArrayList<UsuarioCompartilhado> ?: arrayListOf()
        idNota = intent.getLongExtra("idNota", 0L)

        adapter = AmigosAdapter(amigos) { usuario ->
            removerUsuario(usuario)
        }
        recyclerView.adapter = adapter
    }

    private fun removerUsuario(usuario: UsuarioCompartilhado) {
        val prefs = getSharedPreferences("notesync_prefs", MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        if (token == null) {
            Toast.makeText(this, "Token não encontrado", Toast.LENGTH_SHORT).show()
            return
        }
        val jsonBody = JSONObject().apply {
            put("idNota", idNota)
            put("idUsersComp", org.json.JSONArray().apply { put(usuario.idUsuario) })
        }
        println("[REMOVER AMIGO] Enviando para API: $jsonBody")
        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://d2gmlnphe8ordg.cloudfront.net/api/notesync/nota/remover-amigo")
            .addHeader("Authorization", "Bearer $token")
            .put(requestBody)
            .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("[REMOVER AMIGO] Falha na requisição: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AmigosCompartilhadosActivity, "Erro ao remover usuário", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println("[REMOVER AMIGO] Código: ${response.code}, Body: $responseBody")
                if (response.isSuccessful) {
                    runOnUiThread {
                        amigos.remove(usuario)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this@AmigosCompartilhadosActivity, "Usuário removido!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@AmigosCompartilhadosActivity, "Erro ao remover usuário", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun finish() {
        val resultIntent = Intent()
        resultIntent.putExtra("usuariosCompartilhadosList", amigos)
        setResult(RESULT_OK, resultIntent)
        super.finish()
    }
}
