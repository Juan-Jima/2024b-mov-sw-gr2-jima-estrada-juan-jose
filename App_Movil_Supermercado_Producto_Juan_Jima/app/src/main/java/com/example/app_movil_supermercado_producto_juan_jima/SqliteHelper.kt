package com.example.app_movil_supermercado_producto_juan_jima


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context?) : SQLiteOpenHelper(
    context,
    "SupermercadosDB",
    null,
    3 // Incrementa la versión si cambias la estructura
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSqlCrearSupermercado = """
            CREATE TABLE Supermercado (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(250),
                direccion VARCHAR(250),
                activo BOOLEAN,
                ingresosMensuales DOUBLE
            )
        """.trimIndent()
        db?.execSQL(scriptSqlCrearSupermercado)

        val scriptSqlCrearProducto = """
            CREATE TABLE Producto (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(255),
                precio DOUBLE,
                cantidad INTEGER,
                supermercado_id INTEGER,
                FOREIGN KEY (supermercado_id) REFERENCES Supermercado(id) ON DELETE CASCADE
            )
        """.trimIndent()
        db?.execSQL(scriptSqlCrearProducto)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS Producto")
            db?.execSQL("DROP TABLE IF EXISTS Supermercado")
            onCreate(db) // Recrea las tablas
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true) // Habilita las claves foráneas
    }
}
