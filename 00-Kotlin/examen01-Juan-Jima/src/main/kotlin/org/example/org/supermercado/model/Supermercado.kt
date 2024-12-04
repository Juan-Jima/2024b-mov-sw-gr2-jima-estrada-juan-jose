package org.example.org.example.org.example.main

package org.example.supermercado.model

data class Supermercado(
    val nombre: String,
    val numeroDeProductos: Int,
    val productos: MutableList<Producto>
)
