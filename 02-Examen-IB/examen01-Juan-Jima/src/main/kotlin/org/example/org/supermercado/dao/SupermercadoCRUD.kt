package org.example.supermercado.dao

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.example.supermercado.model.Producto
import org.example.supermercado.model.Supermercado
import java.io.File
import java.io.IOException
import java.lang.reflect.Type

class SupermercadoCRUD {
    private val archivoSupermercados = "supermercados.json"
    private val archivoProductos = "productos.json"
    private val gson = Gson()

    init {
        // Crear archivos si no existen
        File(archivoSupermercados).createNewFile()
        File(archivoProductos).createNewFile()
    }

    // Métodos para Supermercado

    fun generarIdSupermercado(): Int {
        val supermercados = leerSupermercados()
        return if (supermercados.isEmpty()) 1 else supermercados.maxOf { it.id } + 1
    }

    fun crearSupermercado(supermercado: Supermercado) {
        try {
            val supermercados = leerSupermercados().toMutableList()
            supermercados.add(supermercado)
            // Escribir los supermercados en el archivo
            File(archivoSupermercados).writeText(gson.toJson(supermercados))
        } catch (e: IOException) {
            println("Error al crear supermercado: ${e.message}")
        }
    }

    fun leerSupermercados(): List<Supermercado> {
        return try {
            val lineas = File(archivoSupermercados).readText()
            val type: Type = object : TypeToken<List<Supermercado>>() {}.type
            gson.fromJson(lineas, type) ?: emptyList()
        } catch (e: IOException) {
            emptyList()
        }
    }

    fun eliminarSupermercado(nombreSupermercado: String) {
        val supermercados = leerSupermercados().toMutableList()
        val supermercado = supermercados.find { it.nombre == nombreSupermercado }
        if (supermercado != null) {
            supermercados.remove(supermercado)
            File(archivoSupermercados).writeText(gson.toJson(supermercados))
        } else {
            throw IllegalArgumentException("Supermercado no encontrado.")
        }
    }

    // Métodos para Producto

    fun generarIdProducto(): Int {
        val productos = leerProductos()
        return if (productos.isEmpty()) 1 else productos.maxOf { it.id } + 1
    }

    fun crearProducto(producto: Producto, supermercado: Supermercado) {
        try {
            val productos = leerProductos().toMutableList()
            productos.add(producto)
            supermercado.productos.add(producto) // Agregar producto al supermercado
            // Escribir los productos solo si la lista no está vacía
            if (productos.isNotEmpty()) {
                File(archivoProductos).writeText(gson.toJson(productos))
            }
            // Actualizar el supermercado en el archivo
            actualizarSupermercado(supermercado)
        } catch (e: IOException) {
            println("Error al crear producto: ${e.message}")
        }
    }

    fun leerProductos(): List<Producto> {
        return try {
            val lineas = File(archivoProductos).readText()
            val type: Type = object : TypeToken<List<Producto>>() {}.type
            gson.fromJson(lineas, type) ?: emptyList()
        } catch (e: IOException) {
            emptyList()
        }
    }

    fun actualizarProducto(producto: Producto) {
        val productos = leerProductos().toMutableList()
        val index = productos.indexOfFirst { it.id == producto.id }
        if (index != -1) {
            productos[index] = producto
            // Escribir los productos solo si la lista no está vacía
            if (productos.isNotEmpty()) {
                File(archivoProductos).writeText(gson.toJson(productos))
            }
        } else {
            throw IllegalArgumentException("Producto no encontrado.")
        }
    }

    fun eliminarProducto(idProducto: Int) {
        try {
            val productos = leerProductos().toMutableList()
            val producto = productos.find { it.id == idProducto }
            if (producto != null) {
                productos.remove(producto)
                // Escribir la lista actualizada de productos, incluso si está vacía
                File(archivoProductos).writeText(gson.toJson(productos))
            } else {
                throw IllegalArgumentException("Producto no encontrado.")
            }
        } catch (e: IOException) {
            println("Error al eliminar producto: ${e.message}")
        }
    }


    // Función auxiliar para actualizar supermercado en el archivo
    private fun actualizarSupermercado(supermercado: Supermercado) {
        val supermercados = leerSupermercados().toMutableList()
        val index = supermercados.indexOfFirst { it.id == supermercado.id }
        if (index != -1) {
            supermercados[index] = supermercado
            File(archivoSupermercados).writeText(gson.toJson(supermercados))
        }
    }
}
