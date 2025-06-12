package com.example.treino1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AmigosCompartilhadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amigos_compartilhados)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAmigos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        @Suppress("UNCHECKED_CAST")
        val amigos = intent.getSerializableExtra("usuariosCompartilhadosList") as? ArrayList<UsuarioCompartilhado> ?: arrayListOf()
        recyclerView.adapter = AmigosAdapter(amigos)
    }
}

