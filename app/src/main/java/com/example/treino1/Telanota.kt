package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.io.*

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

    private fun salvarNota(nomeArquivo: String, titulo: String, conteudo: String) {
        try {
            openFileOutput(nomeArquivo, MODE_PRIVATE).use {
                it.write((titulo + "\n" + conteudo).toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun lerNota(nomeArquivo: String): Pair<String, String> {
        return try {
            val linhas = openFileInput(nomeArquivo).bufferedReader().readLines()
            val titulo = linhas.firstOrNull() ?: ""
            val conteudo = linhas.drop(1).joinToString("\n")
            titulo to conteudo
        } catch (e: Exception) {
            "" to ""
        }
    }
}
