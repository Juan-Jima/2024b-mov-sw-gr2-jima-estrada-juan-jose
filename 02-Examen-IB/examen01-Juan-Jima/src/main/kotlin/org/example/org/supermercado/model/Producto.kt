package org.example.supermercado.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val fechaCaducidad: String,
    val enStock: Boolean
)
