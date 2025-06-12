package com.example.treino1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AmigosAdapter(private val amigos: List<UsuarioCompartilhado>) : RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return AmigoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {
        val amigo = amigos[position]
        holder.nome.text = amigo.nome
        holder.id.text = "ID: ${amigo.idUsuario}"
    }

    override fun getItemCount() = amigos.size

    class AmigoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(android.R.id.text1)
        val id: TextView = itemView.findViewById(android.R.id.text2)
    }
}

