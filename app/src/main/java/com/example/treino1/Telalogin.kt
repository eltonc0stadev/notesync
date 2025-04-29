package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class Telalogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.telalogin)

        val botaoConfirmar = findViewById<TextView>(R.id.cadastrobuttom)

        botaoConfirmar.setOnClickListener {
            val intent = Intent(this, Telacadastro::class.java)
            startActivity(intent)
        }
        val botaoConectar = findViewById<ImageView>(R.id.conectar)

        botaoConectar.setOnClickListener {
            val intent = Intent(this, Janelaprincipal::class.java)
            startActivity(intent)
        }
    }
}