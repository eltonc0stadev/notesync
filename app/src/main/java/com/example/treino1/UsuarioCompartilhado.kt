package com.example.treino1
import java.io.Serializable

data class UsuarioCompartilhado(
    val idUsuario: Long,
    val nome: String
) : Serializable
