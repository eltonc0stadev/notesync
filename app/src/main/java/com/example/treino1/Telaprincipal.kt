package com.example.treino1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Telaprincipal : AppCompatActivity() {

    private lateinit var containerNotas: GridLayout
    private var filtroAtivo = false

    private lateinit var iconeComunidade: ImageView
    private lateinit var iconeTemaChange: ImageView
    private lateinit var iconeNotificacao: ImageView
    private lateinit var lupaPesquisa: ImageView
    private lateinit var barraPesquisa: EditText
    private lateinit var filtroFavorito: ImageView

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        carregarNotas()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.telaprincipal)

        containerNotas = findViewById(R.id.containerNotas)
        val botaoAdicionarNota = findViewById<ImageView>(R.id.imageView20)
        iconeComunidade = findViewById(R.id.comunidade)
        iconeTemaChange = findViewById(R.id.temachange)
        iconeNotificacao = findViewById(R.id.imageView13)
        lupaPesquisa = findViewById(R.id.Pesquisa)
        filtroFavorito = findViewById(R.id.FiltroFavorito)
        barraPesquisa = findViewById(R.id.campoPesquisa)
        barraPesquisa.visibility = View.GONE

        carregarNotas()

        botaoAdicionarNota.setOnClickListener {
            criarNovaNota()
        }

        val perfil = findViewById<ImageView>(R.id.iconeperfil)
        perfil.setOnClickListener {
            val intent = Intent(this, Telaperfil::class.java)
            startActivity(intent)
        }

        filtroFavorito.setOnClickListener {
            filtroAtivo = !filtroAtivo
            filtroFavorito.alpha = if (filtroAtivo) 1.0f else 0.5f
            carregarNotas(barraPesquisa.text.toString())
        }
        filtroFavorito.alpha = 0.5f

        lupaPesquisa.setOnClickListener {
            if (barraPesquisa.visibility == View.GONE) {
                barraPesquisa.visibility = View.VISIBLE
                barraPesquisa.requestFocus()
                iconeComunidade.visibility = View.INVISIBLE
                iconeTemaChange.visibility = View.INVISIBLE
                iconeNotificacao.visibility = View.INVISIBLE
            } else {
                barraPesquisa.setText("")
                barraPesquisa.visibility = View.GONE
                iconeComunidade.visibility = View.VISIBLE
                iconeTemaChange.visibility = View.VISIBLE
                iconeNotificacao.visibility = View.VISIBLE
                carregarNotas()
            }
        }

        barraPesquisa.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                carregarNotas(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun criarNovaNota() {
        val nomeArquivoNota = "nota_${System.currentTimeMillis()}.txt"
        salvarNota(nomeArquivoNota, "", "", false)
        enviarNotaParaApi(nomeArquivoNota)
        val intent = Intent(this, Telanota::class.java)
        intent.putExtra("nomeArquivo", nomeArquivoNota)
        launcher.launch(intent)
    }

    private fun carregarNotas(filtroTexto: String = "") {
        containerNotas.removeAllViews()
        val prefs = getSharedPreferences("notesync_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        if (token == null) {
            Toast.makeText(this, "Token não encontrado", Toast.LENGTH_SHORT).show()
            return
        }
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://d2gmlnphe8ordg.cloudfront.net/api/notesync/nota/listar") // Substitua pelo endpoint correto
            .addHeader("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@Telaprincipal, "Erro ao buscar notas", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200 || response.code == 302) {
                    val body = response.body?.string()
                    if (body != null) {
                        try {
                            val notasArray = org.json.JSONArray(body)
                            runOnUiThread {
                                for (i in 0 until notasArray.length()) {
                                    val nota = notasArray.getJSONObject(i)
                                    val titulo = nota.optString("titulo", "Sem título")
                                    if (filtroTexto.isBlank() || titulo.contains(filtroTexto, ignoreCase = true)) {
                                        adicionarNotaApiNaTela(nota)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@Telaprincipal, "Erro ao processar notas", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@Telaprincipal, "Erro ao buscar notas: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun adicionarNotaApiNaTela(nota: org.json.JSONObject) {
        val inflater = LayoutInflater.from(this)
        val novaNota = inflater.inflate(R.layout.gruponota, containerNotas, false)
        val titulo = nota.optString("titulo", "Sem título")
        val conteudo = nota.optString("conteudo", "")
        val textTituloNota = novaNota.findViewById<TextView>(R.id.TituloPrincipal)
        val textoLimitado = if (titulo.length > 20) {
            textTituloNota.textSize = 16f
            titulo.substring(0, 20) + "..."
        } else {
            textTituloNota.textSize = 17f
            titulo
        }
        textTituloNota.text = textoLimitado
        val blocoNota = novaNota.findViewById<ImageView>(R.id.bloconota)
        blocoNota.setOnClickListener {
            Toast.makeText(this, conteudo, Toast.LENGTH_SHORT).show()
        }
        // Ajuste de layout para GridLayout
        val params = GridLayout.LayoutParams().apply {
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }
        novaNota.layoutParams = params
        containerNotas.addView(novaNota)
    }

    private fun enviarNotaParaApi(nomeArquivo: String) {
        val jwtToken = getSharedPreferences("auth", MODE_PRIVATE).getString("jwt", "") ?: ""
        if (jwtToken.isBlank()) {
            runOnUiThread {
                Toast.makeText(this, "Token JWT não encontrado", Toast.LENGTH_SHORT).show()
            }
            return
        }

        val linhas = try {
            openFileInput(nomeArquivo).bufferedReader().readLines()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        val titulo = linhas.firstOrNull() ?: ""
        val conteudo = linhas.drop(1).filterNot { it.startsWith("#FAVORITO") }.joinToString("\n")
        val favorito = linhas.any { it.trim().equals("#FAVORITO=true", ignoreCase = true) }

        val jsonBody = JSONObject().apply {
            put("titulo", titulo)
            put("conteudo", conteudo)
            put("favorito", favorito)
        }

        val requestBody = jsonBody.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://d2gmlnphe8ordg.cloudfront.net/nota/criar")
            .addHeader("Authorization", "Bearer $jwtToken")
            .post(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@Telaprincipal, "Erro ao enviar nota", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Telaprincipal, "Nota enviada com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Telaprincipal, "Erro: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun salvarNota(nomeArquivo: String, titulo: String, conteudo: String, favorito: Boolean) {
        // Função placeholder para evitar erro de referência, não faz nada
    }
}
