package com.example.treino1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        @Suppress("DEPRECATION")
        val lista = intent.getParcelableArrayListExtra<UsuarioCompartilhado>("usuariosCompartilhadosList")
        amigos = if (lista != null) ArrayList(lista) else arrayListOf()
        idNota = intent.getLongExtra("idNota", 0L)

        adapter = AmigosAdapter(amigos) { usuario ->
            removerUsuario(usuario)
        }
        recyclerView.adapter = adapter

        val btnAdicionar = findViewById<Button>(R.id.btnAdicionarAmigo)
        btnAdicionar.setOnClickListener {
            val input = EditText(this)
            input.hint = "ID do usuário"
            AlertDialog.Builder(this)
                .setTitle("Adicionar usuário compartilhado")
                .setView(input)
                .setPositiveButton("Adicionar") { dialog, _ ->
                    val idStr = input.text.toString().trim()
                    val idUser = idStr.toLongOrNull()
                    if (idUser != null) {
                        adicionarUsuario(idUser)
                    } else {
                        Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        val btnVoltar = findViewById<android.widget.ImageButton>(R.id.btnVoltarNota)
        btnVoltar.setOnClickListener {
            finish()
        }
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

    private fun adicionarUsuario(idUser: Long) {
        val prefs = getSharedPreferences("notesync_prefs", MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        if (token == null) {
            Toast.makeText(this, "Token não encontrado", Toast.LENGTH_SHORT).show()
            return
        }
        val jsonBody = JSONObject().apply {
            put("idNota", idNota)
            put("idUsersComp", org.json.JSONArray().apply { put(idUser) })
        }
        println("[ADICIONAR AMIGO] Enviando para API: $jsonBody")
        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://d2gmlnphe8ordg.cloudfront.net/api/notesync/nota/adicionar-amigo")
            .addHeader("Authorization", "Bearer $token")
            .put(requestBody)
            .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("[ADICIONAR AMIGO] Falha na requisição: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AmigosCompartilhadosActivity, "Erro ao adicionar usuário", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println("[ADICIONAR AMIGO] Código: ${response.code}, Body: $responseBody")
                if (response.isSuccessful && responseBody != null) {
                    try {
                        val obj = JSONObject(responseBody)
                        val novoUsuarioArray = obj.optJSONArray("usuariosCompartilhados")
                        val novos = mutableListOf<UsuarioCompartilhado>()
                        if (novoUsuarioArray != null) {
                            for (i in 0 until novoUsuarioArray.length()) {
                                val u = novoUsuarioArray.getJSONObject(i)
                                novos.add(UsuarioCompartilhado(u.getLong("idUsuario"), u.getString("nome")))
                            }
                        }
                        runOnUiThread {
                            amigos.clear()
                            amigos.addAll(novos)
                            adapter.notifyDataSetChanged()
                            Toast.makeText(this@AmigosCompartilhadosActivity, "Usuário adicionado!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@AmigosCompartilhadosActivity, "Erro ao processar resposta", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@AmigosCompartilhadosActivity, "Erro ao adicionar usuário", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun finish() {
        val resultIntent = Intent()
        resultIntent.putParcelableArrayListExtra("usuariosCompartilhadosList", amigos)
        setResult(RESULT_OK, resultIntent)
        super.finish()
    }
}
