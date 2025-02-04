package com.example.app_movil_supermercado_producto_juan_jima

data class Supermercado(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val activo: Boolean, // Este es el campo que debería ser Booleano
    val ingresosMensuales: Double,
    val ubicacion: String // La ubicación como String "latitud, longitud"
)
