package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Telaprincipal : AppCompatActivity() {

    private lateinit var containerNotas: GridLayout

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        carregarNotasSalvas()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.telaprincipal)

        containerNotas = findViewById(R.id.containerNotas)
        val botaoAdicionarNota = findViewById<ImageView>(R.id.imageView20)

        carregarNotasSalvas()

        botaoAdicionarNota.setOnClickListener {
            criarNovaNota()
        }
    }

    private fun criarNovaNota() {
        val nomeArquivoNota = "nota_${System.currentTimeMillis()}.txt"

        salvarNota(nomeArquivoNota, "Nova Nota", "")

        val intent = Intent(this, Telanota::class.java)
        intent.putExtra("nomeArquivo", nomeArquivoNota)
        launcher.launch(intent)
    }

    private fun carregarNotasSalvas() {
        containerNotas.removeAllViews()

        val arquivos = filesDir.listFiles() ?: return

        val arquivosNotas = arquivos.filter { it.name.startsWith("nota_") && it.name.endsWith(".txt") }

        for (arquivo in arquivosNotas) {
            adicionarNotaNaTela(arquivo.name)
        }
    }

    private fun adicionarNotaNaTela(nomeArquivoNota: String) {
        val inflater = LayoutInflater.from(this)
        val novaNota = inflater.inflate(R.layout.gruponota, containerNotas, false)

        val tituloNota = lerTituloDaNota(nomeArquivoNota)
        val textTituloNota = novaNota.findViewById<TextView>(R.id.TituloPrincipal)
        val textoLimitado = if (tituloNota.length > 20) {
            textTituloNota.textSize = 16f
            tituloNota.substring(0, 20) + "..."
        } else {
            textTituloNota.textSize = 17f
            tituloNota
        }
        textTituloNota.text = textoLimitado

        val blocoNota = novaNota.findViewById<ImageView>(R.id.bloconota)
        blocoNota.setOnClickListener {
            val intent = Intent(this, Telanota::class.java)
            intent.putExtra("nomeArquivo", nomeArquivoNota)
            launcher.launch(intent)
        }

        val layoutParams = GridLayout.LayoutParams().apply {
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }
        novaNota.layoutParams = layoutParams

        containerNotas.addView(novaNota)
    }

    private fun lerTituloDaNota(nomeArquivo: String): String {
        return try {
            val linhas = openFileInput(nomeArquivo).bufferedReader().readLines()
            linhas.firstOrNull() ?: "Nova Nota"
        } catch (e: Exception) {
            "Nova Nota"
        }
    }

    private fun salvarNota(nomeArquivo: String, titulo: String, conteudo: String) {
        try {
            openFileOutput(nomeArquivo, MODE_PRIVATE).use {
                it.write((titulo + "\n" + conteudo).toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
