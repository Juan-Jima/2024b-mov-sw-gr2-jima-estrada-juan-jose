package com.example.app_movil_supermercado_producto_juan_jima

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context?) : SQLiteOpenHelper(
    context,
    "SupermercadosDB",
    null,
    5 // Incrementamos la versión de la base de datos a 5
) {
    override fun onCreate(db: SQLiteDatabase?) {
        // Script para crear la tabla Supermercado
        val scriptSqlCrearSupermercado = """
            CREATE TABLE Supermercado (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(250),
                direccion VARCHAR(250),
                activo BOOLEAN,
                ingresosMensuales DOUBLE,
                ubicacion TEXT -- Nueva columna como String en formato "latitud, longitud"
            )
        """.trimIndent()
        db?.execSQL(scriptSqlCrearSupermercado)

        // Script para crear la tabla Producto con la columna 'stock' en lugar de 'cantidad'
        val scriptSqlCrearProducto = """
            CREATE TABLE Producto (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(255),
                precio DOUBLE,
                stock INTEGER,  -- Cambié 'cantidad' por 'stock'
                supermercado_id INTEGER,
                ubicacion TEXT,  -- Nueva columna para la ubicación
                FOREIGN KEY (supermercado_id) REFERENCES Supermercado(id) ON DELETE CASCADE
            )
        """.trimIndent()
        db?.execSQL(scriptSqlCrearProducto)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            // Realizamos la actualización de la base de datos
            if (oldVersion == 4) {
                // Renombramos la tabla Producto antigua
                db?.execSQL("ALTER TABLE Producto RENAME TO Producto_old")

                // Creamos la nueva tabla Producto con la columna 'stock' en lugar de 'cantidad'
                val scriptSqlCrearProductoNuevo = """
                    CREATE TABLE Producto (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nombre VARCHAR(255),
                        precio DOUBLE,
                        stock INTEGER,  -- Cambié 'cantidad' por 'stock'
                        supermercado_id INTEGER,
                        ubicacion TEXT,  -- Nueva columna para la ubicación
                        FOREIGN KEY (supermercado_id) REFERENCES Supermercado(id) ON DELETE CASCADE
                    )
                """.trimIndent()
                db?.execSQL(scriptSqlCrearProductoNuevo)

                // Copiamos los datos de la tabla antigua a la nueva
                db?.execSQL("""
                    INSERT INTO Producto (id, nombre, precio, stock, supermercado_id, ubicacion)
                    SELECT id, nombre, precio, cantidad AS stock, supermercado_id, ubicacion FROM Producto_old
                """.trimIndent())

                // Eliminamos la tabla antigua
                db?.execSQL("DROP TABLE Producto_old")
            }
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true) // Habilita las claves foráneas
    }
}
