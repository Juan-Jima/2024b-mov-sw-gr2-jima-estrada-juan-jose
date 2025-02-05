package com.example.agendaestudiantil

data class Task(
    val id: Int? = null, // Hacer que id sea opcional
    var title: String,    // El título de la tarea
    var description: String,  // Descripción de la tarea
    var date: String      // Fecha de la tarea
)