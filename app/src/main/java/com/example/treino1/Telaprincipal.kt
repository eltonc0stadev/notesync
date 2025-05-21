package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class Telaprincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.telaprincipal)


        val layoutincluido = findViewById<View>(R.id.include_grupo_nota)

        val testedenota = layoutincluido.findViewById<ImageView>(R.id.bloconota)

        testedenota.setOnClickListener {
            val intent = Intent(this, Telanota::class.java)
            startActivity(intent)
        }
//codigo faz com q mude as imagens
        val favoritoemp = layoutincluido.findViewById<ImageView>(R.id.favoritoempty)
        val favoritofull = layoutincluido.findViewById<ImageView>(R.id.favselecionado)

        val lockopen = layoutincluido.findViewById<ImageView>(R.id.lockopen)
        val lockclose = layoutincluido.findViewById<ImageView>(R.id.lockclose)

        favoritoemp.setOnClickListener {
            favoritoemp.visibility = View.INVISIBLE
            favoritofull.visibility = View.VISIBLE
        }
        favoritofull.setOnClickListener {
            favoritofull.visibility = View.INVISIBLE
            favoritoemp.visibility = View.VISIBLE
        }
        lockopen.setOnClickListener {
            lockopen.visibility = View.INVISIBLE
            lockclose.visibility = View.VISIBLE
        }
        lockclose.setOnClickListener {
            lockclose.visibility = View.INVISIBLE
            lockopen.visibility = View.VISIBLE
        }

        val perfil = findViewById<ImageView>(R.id.iconeperfil)

        perfil.setOnClickListener {
            val intentperf = Intent(this, Telaperfil::class.java)
            startActivity(intentperf)
        }

        val textTituloPrincipal = findViewById<TextView>(R.id.TituloPrincipal)

        val tituloSalvo = lerTituloDoArquivo("titulo.txt")

        val textoLimitado = if (tituloSalvo.length > 20) {
            textTituloPrincipal.textSize = 14f
            tituloSalvo.substring(0, 20) + "..."
        } else {
            textTituloPrincipal.textSize = 15f
            tituloSalvo
        }

        textTituloPrincipal.text = textoLimitado
    }

    private fun lerTituloDoArquivo(nomeArquivo: String): String {
        return try {
            openFileInput(nomeArquivo).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            "Título não encontrado"
        }
    }

}