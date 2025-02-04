package com.example.app_movil_supermercado_producto_juan_jima

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Controlador(context: Context) {
    private val dbHelper = SqliteHelper(context)

    // Crear Producto
    fun crearProducto(supermercadoId: Int, producto: Producto) {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("precio", producto.precio)
            put("stock", producto.stock) // Cambié 'cantidad' por 'stock'
            put("supermercado_id", supermercadoId)
        }
        db.insert("Producto", null, valores)
        db.close()
    }

    // Actualizar Producto
    fun actualizarProducto(producto: Producto): Boolean {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("precio", producto.precio)
            put("stock", producto.stock) // Cambié 'cantidad' por 'stock'
        }
        val rows = db.update(
            "Producto",
            valores,
            "id = ?",
            arrayOf(producto.id.toString())
        )
        db.close()
        return rows > 0
    }

    // Listar Productos por Supermercado
    fun listarProductosPorSupermercado(supermercadoId: Int): List<Producto> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Producto",
            arrayOf("id", "nombre", "precio", "stock", "supermercado_id"), // Cambié 'cantidad' por 'stock'
            "supermercado_id = ?",
            arrayOf(supermercadoId.toString()),
            null, null, null
        )

        val productos = mutableListOf<Producto>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val nombre = getString(getColumnIndexOrThrow("nombre"))
                val precio = getDouble(getColumnIndexOrThrow("precio"))
                val stock = getInt(getColumnIndexOrThrow("stock")) // Cambié 'cantidad' por 'stock'
                val supermercadoId = getInt(getColumnIndexOrThrow("supermercado_id"))

                productos.add(Producto(id, nombre, precio, stock, supermercadoId))
            }
        }
        cursor.close()
        db.close()
        return productos
    }

    // Crear Supermercado
    fun crearSupermercado(supermercado: Supermercado) {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", supermercado.nombre)
            put("direccion", supermercado.direccion)
            put("activo", if (supermercado.activo) 1 else 0) // Convertir Boolean a 1 o 0
            put("ingresosMensuales", supermercado.ingresosMensuales)
            put("ubicacion", supermercado.ubicacion) // Aquí pasas la ubicación como texto "latitud, longitud"
        }
        db.insert("Supermercado", null, valores)
        db.close()
    }

    // Actualizar Supermercado
    fun actualizarSupermercado(supermercado: Supermercado): Boolean {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", supermercado.nombre)
            put("direccion", supermercado.direccion)
            put("activo", if (supermercado.activo) 1 else 0) // Convertir Boolean a 1 o 0
            put("ingresosMensuales", supermercado.ingresosMensuales)
            put("ubicacion", supermercado.ubicacion) // Actualizamos la ubicación
        }
        val rows = db.update(
            "Supermercado",
            valores,
            "id = ?",
            arrayOf(supermercado.id.toString())
        )
        db.close()
        return rows > 0
    }

    // Listar todos los Supermercados
    fun listarSupermercados(): List<Supermercado> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Supermercado",
            arrayOf("id", "nombre", "direccion", "activo", "ingresosMensuales", "ubicacion"),
            null, null, null, null, null
        )

        val supermercados = mutableListOf<Supermercado>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val nombre = getString(getColumnIndexOrThrow("nombre"))
                val direccion = getString(getColumnIndexOrThrow("direccion"))
                val activo = getInt(getColumnIndexOrThrow("activo")) == 1 // Convertir a Boolean
                val ingresosMensuales = getDouble(getColumnIndexOrThrow("ingresosMensuales"))
                val ubicacion = getString(getColumnIndexOrThrow("ubicacion"))

                supermercados.add(Supermercado(id, nombre, direccion, activo, ingresosMensuales, ubicacion))
            }
        }
        cursor.close()
        db.close()
        return supermercados
    }

    // Obtener Supermercado por ID
    fun obtenerSupermercadoPorId(id: Int): Supermercado? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Supermercado",
            arrayOf("id", "nombre", "direccion", "activo", "ingresosMensuales", "ubicacion"),
            "id = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var supermercado: Supermercado? = null
        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"))
            val activo = cursor.getInt(cursor.getColumnIndexOrThrow("activo")) == 1 // Convertir a Boolean
            val ingresosMensuales = cursor.getDouble(cursor.getColumnIndexOrThrow("ingresosMensuales"))
            val ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"))
            supermercado = Supermercado(id, nombre, direccion, activo, ingresosMensuales, ubicacion)
        }
        cursor.close()
        db.close()
        return supermercado
    }

    // Eliminar Producto
    fun eliminarProducto(productoId: Int): Boolean {
        val db = dbHelper.writableDatabase
        val rows = db.delete(
            "Producto",
            "id = ?",
            arrayOf(productoId.toString())
        )
        db.close()
        return rows > 0
    }

    // Eliminar Supermercado
    fun eliminarSupermercado(supermercadoId: Int): Boolean {
        val db = dbHelper.writableDatabase
        val rows = db.delete(
            "Supermercado",
            "id = ?",
            arrayOf(supermercadoId.toString())
        )
        db.close()
        return rows > 0
    }
}
