package com.example.notesync;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int idUsuario;
    private String nome;

    public Usuario(int idUsuario, String nome) {
        this.idUsuario = idUsuario;
        this.nome = nome;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }
}

