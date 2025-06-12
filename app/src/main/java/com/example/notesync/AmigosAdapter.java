package com.example.notesync;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder> {
    private List<Usuario> amigos;

    public AmigosAdapter(List<Usuario> amigos) {
        this.amigos = amigos;
    }

    @NonNull
    @Override
    public AmigoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new AmigoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmigoViewHolder holder, int position) {
        holder.nome.setText(amigos.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return amigos.size();
    }

    static class AmigoViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        AmigoViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(android.R.id.text1);
        }
    }
}

