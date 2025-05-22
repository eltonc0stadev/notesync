package com.example.treino1

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.io.File

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
        val intent = Intent(this, Telanota::class.java)
        intent.putExtra("nomeArquivo", nomeArquivoNota)
        launcher.launch(intent)
    }

    private fun carregarNotas(filtroTexto: String = "") {
        containerNotas.removeAllViews()
        val arquivos = filesDir.listFiles() ?: return
        val arquivosNotas = arquivos.filter { it.name.startsWith("nota_") && it.name.endsWith(".txt") }

        for (arquivo in arquivosNotas) {
            val nome = arquivo.name
            val titulo = lerTituloDaNota(nome)
            val passaFiltroFavorito = !filtroAtivo || lerFavoritoDaNota(nome)
            val passaFiltroTexto = filtroTexto.isBlank() || titulo.contains(filtroTexto, ignoreCase = true)

            if (passaFiltroFavorito && passaFiltroTexto) {
                adicionarNotaNaTela(nome)
            }
        }
    }

    private fun adicionarNotaNaTela(nomeArquivoNota: String) {
        val inflater = LayoutInflater.from(this)
        val novaNota = inflater.inflate(R.layout.gruponota, containerNotas, false)

        val tituloNota = lerTituloDaNota(nomeArquivoNota).ifBlank { "Sem título" }
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

        val favoritoEmpty = novaNota.findViewById<ImageView>(R.id.favoritoempty)
        val favoritoSelecionado = novaNota.findViewById<ImageView>(R.id.favselecionado)

        val estaFavoritado = lerFavoritoDaNota(nomeArquivoNota)
        if (estaFavoritado) {
            favoritoEmpty.visibility = View.INVISIBLE
            favoritoSelecionado.visibility = View.VISIBLE
        } else {
            favoritoEmpty.visibility = View.VISIBLE
            favoritoSelecionado.visibility = View.INVISIBLE
        }

        favoritoEmpty.setOnClickListener {
            favoritoEmpty.visibility = View.INVISIBLE
            favoritoSelecionado.visibility = View.VISIBLE
            atualizarFavorito(nomeArquivoNota, true)
        }

        favoritoSelecionado.setOnClickListener {
            favoritoSelecionado.visibility = View.INVISIBLE
            favoritoEmpty.visibility = View.VISIBLE
            atualizarFavorito(nomeArquivoNota, false)
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
            linhas.firstOrNull() ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun lerFavoritoDaNota(nomeArquivo: String): Boolean {
        return try {
            val linhas = openFileInput(nomeArquivo).bufferedReader().readLines()
            linhas.any { it.trim().equals("#FAVORITO=true", ignoreCase = true) }
        } catch (e: Exception) {
            false
        }
    }

    private fun salvarNota(nomeArquivo: String, titulo: String, conteudo: String, favorito: Boolean) {
        try {
            openFileOutput(nomeArquivo, MODE_PRIVATE).use {
                val favoritoTag = if (favorito) "\n#FAVORITO=true" else ""
                it.write((titulo + "\n" + conteudo + favoritoTag).toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun atualizarFavorito(nomeArquivo: String, favoritar: Boolean) {
        try {
            val arquivo = File(filesDir, nomeArquivo)
            if (!arquivo.exists()) return

            val linhas = arquivo.readLines().toMutableList()
            linhas.removeAll { it.trim().startsWith("#FAVORITO") }

            if (favoritar) {
                linhas.add("#FAVORITO=true")
            }

            arquivo.writeText(linhas.joinToString("\n"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
