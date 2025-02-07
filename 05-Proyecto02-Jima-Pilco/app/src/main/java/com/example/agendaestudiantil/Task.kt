package com.example.agendaestudiantil

import android.os.Parcel
import android.os.Parcelable

// Modelo de datos para la tarea
data class Task(
    val id: Int? = null, // ID opcional, se autogenera en la base de datos
    var title: String, // Título de la tarea
    var description: String, // Descripción de la tarea
    var date: String, // Fecha de la tarea (Formato dd/MM/yyyy)
    var location: String? = null
) : Parcelable {

    // Constructor para Parcelable
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(location)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}
