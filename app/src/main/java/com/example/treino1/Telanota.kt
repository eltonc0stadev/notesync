package com.example.treino1


import android.content.Intent
import android.media.Image
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
            val intent = Intent(this, Janelaprincipal::class.java)
            startActivity(intent)
        }

    }
}