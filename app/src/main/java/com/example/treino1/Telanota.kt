package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class Telanota : AppCompatActivity() {

    private lateinit var anotacao: EditText
    private lateinit var titulo: EditText

    val tituloFile = "titulo.txt"
    val anotacaoFile = "anotacao.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.telanota)

        val voltar = findViewById<ImageButton>(R.id.voltarnota)

        voltar.setOnClickListener {
            val intent = Intent(this, Telaprincipal::class.java)
            startActivity(intent)
        }

        anotacao = findViewById(R.id.editTextText3)
        titulo = findViewById(R.id.Texttitulo)

        titulo.setText(readFromFile(tituloFile))
        anotacao.setText(readFromFile(anotacaoFile))

        titulo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveToFile(tituloFile, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        anotacao.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveToFile(anotacaoFile, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun saveToFile(filename: String, content: String) {
        try {
            openFileOutput(filename, MODE_PRIVATE).use {
                it.write(content.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFromFile(filename: String): String {
        return try {
            openFileInput(filename).bufferedReader().useLines { lines ->
                lines.joinToString("\n")
            }
        } catch (e: FileNotFoundException) {
            ""
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}
