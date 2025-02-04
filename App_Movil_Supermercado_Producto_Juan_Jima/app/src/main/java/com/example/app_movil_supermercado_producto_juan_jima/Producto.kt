package com.example.app_movil_supermercado_producto_juan_jima

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val stock: Int, // Cambio de 'cantidad' a 'stock'
    val supermercadoId: Int // Asegúrate de que esté aquí
)
