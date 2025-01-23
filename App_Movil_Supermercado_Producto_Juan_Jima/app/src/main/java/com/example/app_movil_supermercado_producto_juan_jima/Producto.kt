package com.example.app_movil_supermercado_producto_juan_jima

class Producto(
    val id: Int,
    var nombre: String,
    var precio: Double,
    var cantidad: Int,
    val disponible: Boolean = true
)