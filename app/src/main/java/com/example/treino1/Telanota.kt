package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Base64
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class Telanota : AppCompatActivity() {

    private lateinit var anotacao: EditText
    private lateinit var titulo: EditText
    private var nomeArquivo: String = ""

    private var idNota: Long? = null
    private var usuariosCompartilhadosIds: ArrayList<Long> = arrayListOf()
    private var notaAlterada: Boolean = false

    private lateinit var comunidade: ImageView
    private lateinit var temachange: ImageView
    private lateinit var notificacao: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.telanota)

        anotacao = findViewById(R.id.editTextText3)
        titulo = findViewById(R.id.Texttitulo)

        // Previne quebras de linha no título
        titulo.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source?.toString()?.replace("\n", " ") ?: ""
        })

        // Recebe os dados da nota selecionada
        nomeArquivo = intent.getStringExtra("nomeArquivo") ?: "nota_padrao.txt"
        val tituloRecebido = intent.getStringExtra("titulo") ?: ""
        val conteudoRecebido = intent.getStringExtra("conteudo")?.let { decodificarConteudo(it) } ?: ""
        idNota = intent.getStringExtra("idNota")?.toLongOrNull()

        // Recebe a lista de usuários compartilhados
        @Suppress("UNCHECKED_CAST")
        intent.getSerializableExtra("usuariosCompartilhadosIds")?.let {
            usuariosCompartilhadosIds = it as ArrayList<Long>
        }

        // Define o título e conteúdo nos campos
        titulo.setText(tituloRecebido)
        anotacao.setText(conteudoRecebido)

        // Configura o botão de voltar
        val voltar = findViewById<ImageButton>(R.id.voltarnota)
        voltar.setOnClickListener {
            if (notaAlterada && idNota != null) {
                atualizarNotaApi()
            }
            finalizarEdicao()
        }

        // Configura o botão de lixeira
        val botaoTrash = findViewById<ImageView>(R.id.trash)
        botaoTrash.setOnClickListener {
            mostrarDialogoConfirmacaoApagar()
        }

        // Configura TextWatchers para detectar mudanças
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                notaAlterada = true
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        titulo.addTextChangedListener(textWatcher)
        anotacao.addTextChangedListener(textWatcher)
    }

    private fun finalizarEdicao() {
        val intent = Intent()
        intent.putExtra("nomeArquivo", nomeArquivo)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun atualizarNotaApi() {
        println("Iniciando atualização da nota")
        if (idNota == null) {
            println("idNota é null, não é possível atualizar")
            return
        }

        val prefs = getSharedPreferences("notesync_prefs", MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        if (token == null) {
            println("Token não encontrado")
            return
        }

        val tituloAtual = titulo.text.toString().replace("\n", " ")
        val conteudoAtual = codificarConteudo(anotacao.text.toString())

        val jsonBody = JSONObject().apply {
            put("id", idNota)
            put("titulo", tituloAtual)
            put("conteudo", conteudoAtual)
            put("arquivada", false)
            put("lixeira", false)
            put("usuariosCompartilhadosIds", JSONArray(usuariosCompartilhadosIds))
        }

        println("Enviando atualização para a API:")
        println("Request Body: ${jsonBody.toString(2)}")

        ApiClient.request(
            url = "https://d2gmlnphe8ordg.cloudfront.net/api/notesync/nota/atualizar",
            method = "PUT",
            jsonBody = jsonBody,
            headers = mapOf("Authorization" to "Bearer $token")
        ) { success, response ->
            runOnUiThread {
                if (success) {
                    println("Nota atualizada com sucesso!")
                } else {
                    val errorMessage = "Erro ao atualizar nota: $response"
                    println(errorMessage)
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun codificarConteudo(conteudo: String): String {
        return Base64.encodeToString(conteudo.toByteArray(), Base64.NO_WRAP)
    }

    private fun decodificarConteudo(conteudoBase64: String): String {
        return try {
            String(Base64.decode(conteudoBase64, Base64.DEFAULT))
        } catch (e: Exception) {
            println("Erro ao decodificar conteúdo: ${e.message}")
            ""
        }
    }

    private fun mostrarDialogoConfirmacaoApagar() {
        AlertDialog.Builder(this)
            .setTitle("Excluir nota")
            .setMessage("Tem certeza que deseja apagar esta nota?")
            .setPositiveButton("Sim") { dialog, _ ->
                apagarNota()
                dialog.dismiss()
            }
            .setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun apagarNota() {
        val apagou = deleteFile(nomeArquivo)
        if (apagou) {
            val intent = Intent().apply {
                putExtra("notaApagada", true)
                putExtra("nomeArquivo", nomeArquivo)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        if (notaAlterada && idNota != null) {
            println("Activity entrando em pausa, atualizando nota...")
            atualizarNotaApi()
        }
    }
}
