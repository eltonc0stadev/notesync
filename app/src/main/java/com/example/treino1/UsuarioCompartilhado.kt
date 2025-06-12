package com.example.treino1

import android.os.Parcel
import android.os.Parcelable

data class UsuarioCompartilhado(
    val idUsuario: Long,
    val nome: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(idUsuario)
        parcel.writeString(nome)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<UsuarioCompartilhado> {
        override fun createFromParcel(parcel: Parcel): UsuarioCompartilhado = UsuarioCompartilhado(parcel)
        override fun newArray(size: Int): Array<UsuarioCompartilhado?> = arrayOfNulls(size)
    }
}
