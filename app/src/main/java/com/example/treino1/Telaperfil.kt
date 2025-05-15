package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Telaperfil : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.perfil)

        val voltar = findViewById<ImageButton>(R.id.voltarperfil)

        voltar.setOnClickListener {
            val intent = Intent(this, Telaprincipal::class.java)
            startActivity(intent)
        }
    }
}