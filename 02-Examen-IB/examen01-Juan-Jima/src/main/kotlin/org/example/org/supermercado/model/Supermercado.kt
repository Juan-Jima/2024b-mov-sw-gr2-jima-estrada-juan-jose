package org.example.supermercado.model

data class Supermercado(
    val id: Int,
    val nombre: String,
    val numeroDeProductos: Int,
    val productos: MutableList<Producto>,
    val direccion: String,
    val telefono: String
)
