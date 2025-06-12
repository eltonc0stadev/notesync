package com.example.treino1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AmigosAdapter(
    private val amigos: List<UsuarioCompartilhado>,
    private val onRemoverClick: (UsuarioCompartilhado) -> Unit
) : RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmigoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_amigo_compartilhado, parent, false)
        return AmigoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmigoViewHolder, position: Int) {
        val amigo = amigos[position]
        holder.nome.text = amigo.nome
        holder.id.text = "ID: ${amigo.idUsuario}"
        holder.btnRemover.setOnClickListener {
            onRemoverClick(amigo)
        }
    }

    override fun getItemCount() = amigos.size

    class AmigoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textNomeAmigo)
        val id: TextView = itemView.findViewById(R.id.textIdAmigo)
        val btnRemover: ImageView = itemView.findViewById(R.id.btnRemoverAmigo)
    }
}
