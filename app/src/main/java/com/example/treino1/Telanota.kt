package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException

class Telanota : AppCompatActivity() {

    private lateinit var anotacao: EditText
    private lateinit var titulo: EditText
    private var nomeArquivo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.telanota)

        anotacao = findViewById(R.id.editTextText3)
        titulo = findViewById(R.id.Texttitulo)
        nomeArquivo = intent.getStringExtra("nomeArquivo") ?: "nota_padrao.txt"

        val voltar = findViewById<ImageButton>(R.id.voltarnota)
        voltar.setOnClickListener {
            val intent = Intent()
            intent.putExtra("nomeArquivo", nomeArquivo)
            setResult(RESULT_OK, intent)
            finish()
        }

        val botaoTrash = findViewById<ImageView>(R.id.trash)
        botaoTrash.setOnClickListener {
            mostrarDialogoConfirmacaoApagar()
        }

        val (tituloSalvo, anotacaoSalva) = lerNota(nomeArquivo)
        titulo.setText(tituloSalvo)
        anotacao.setText(anotacaoSalva)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                salvarNota(nomeArquivo, titulo.text.toString(), anotacao.text.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        titulo.addTextChangedListener(textWatcher)
        anotacao.addTextChangedListener(textWatcher)
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

    private fun salvarNota(nomeArquivo: String, titulo: String, conteudo: String) {
        try {
            val arquivo = File(filesDir, nomeArquivo)
            val linhasAtuais = if (arquivo.exists()) arquivo.readLines().toMutableList() else mutableListOf()

            val favoritado = linhasAtuais.any { it.trim().equals("#FAVORITO=true", ignoreCase = true) }

            val novaNota = mutableListOf<String>()
            novaNota.add(titulo)
            novaNota.add(conteudo)
            if (favoritado) novaNota.add("#FAVORITO=true")

            arquivo.writeText(novaNota.joinToString("\n"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun lerNota(nomeArquivo: String): Pair<String, String> {
        return try {
            val linhas = openFileInput(nomeArquivo).bufferedReader().readLines()
            val titulo = linhas.firstOrNull() ?: ""
            val conteudo = linhas
                .drop(1)
                .filterNot { it.trim().startsWith("#FAVORITO") }
                .joinToString("\n")
            titulo to conteudo
        } catch (e: Exception) {
            "" to ""
        }
    }
}
