package com.example.treino1


import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class Telanota : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.telanota)

        val voltar = findViewById<ImageButton>(R.id.voltarnota)

        voltar.setOnClickListener {
            val intent = Intent(this, Telaprincipal::class.java)
            startActivity(intent)
        }

    }
}