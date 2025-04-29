package com.example.treino1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class Telacadastro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.telacadastro)

        val voltarc = findViewById<ImageButton>(R.id.voltarcadastro)

        voltarc.setOnClickListener {
            val intent = Intent(this, Telalogin::class.java)
            startActivity(intent)
        }

    }
}