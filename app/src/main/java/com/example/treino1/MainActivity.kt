package com.example.treino1

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
////        val botaoConectar = findViewById<ImageView>(R.id.imageView3)
////        botaoConectar.setOnClickListener {
////            Toast.makeText(this, "Conectando...", Toast.LENGTH_SHORT).show()
//        }
    }
}