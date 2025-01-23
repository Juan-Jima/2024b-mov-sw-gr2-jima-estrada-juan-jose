package com.example.app_movil_supermercado_producto_juan_jima


import android.content.ContentValues
import android.content.Context

class Controlador (context: Context) {
    private val dbHelper = SqliteHelper(context)

    // Crear Supermercado
    fun crearSupermercado(supermercado: Supermercado) {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("id", supermercado.id)
            put("nombre", supermercado.nombre)
            put("direccion", supermercado.direccion)
            put("ingresosMensuales", supermercado.ingresosMensuales)
        }
        db.insert("Supermercado", null, valores)
        db.close()
    }

    // Listar Supermercados
    fun listarSupermercados(): List<Supermercado> {
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM Supermercado", null)
        val supermercados = mutableListOf<Supermercado>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val nombre = cursor.getString(1)
            val direccion = cursor.getString(2)
            val ingresosMensuales = cursor.getDouble(3)
            supermercados.add(Supermercado(id, nombre, direccion, true, ingresosMensuales))
        }
        cursor.close()
        db.close()
        return supermercados
    }

    // Actualizar Supermercado
    fun actualizarSupermercado(supermercado: Supermercado): Boolean {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", supermercado.nombre)
            put("direccion", supermercado.direccion)
            put("ingresosMensuales", supermercado.ingresosMensuales)
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

    // Crear Producto
    fun crearProducto(supermercadoId: Int, producto: Producto) {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("precio", producto.precio)
            put("cantidad", producto.cantidad)
            put("supermercado_id", supermercadoId)
        }
        db.insert("Producto", null, valores)
        db.close()
    }

    // Listar Productos por Supermercado
    fun listarProductosPorSupermercado(supermercadoId: Int): List<Producto> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM Producto WHERE supermercado_id = ?",
            arrayOf(supermercadoId.toString())
        )
        val productos = mutableListOf<Producto>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
            val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
            productos.add(Producto(id, nombre, precio, cantidad))
        }
        cursor.close()
        db.close()
        return productos
    }

    // Actualizar Producto
    fun actualizarProducto(producto: Producto): Boolean {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", producto.nombre)
            put("precio", producto.precio)
            put("cantidad", producto.cantidad)
        }
        val rows = db.update("Producto", valores, "id = ?", arrayOf(producto.id.toString()))
        db.close()
        return rows > 0
    }

    // Eliminar Producto
    fun eliminarProducto(productoId: Int): Boolean {
        val db = dbHelper.writableDatabase
        val rows = db.delete("Producto", "id = ?", arrayOf(productoId.toString()))
        db.close()
        return rows > 0
    }
}
