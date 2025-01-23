package com.example.app_movil_supermercado_producto_juan_jima

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity




class ActivityAgregarEditarProducto : AppCompatActivity() {
    private lateinit var controlador: Controlador
    private lateinit var etNombreProducto: EditText
    private lateinit var etPrecioProducto: EditText
    private lateinit var etStockProducto: EditText  // Cambiado de etCantidadProducto a etStockProducto
    private lateinit var btnGuardarProducto: Button
    private var supermercadoId: Int = 0
    private var productoId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_editar_producto)

        controlador = Controlador(this)
        etNombreProducto = findViewById(R.id.etNombreProducto)
        etPrecioProducto = findViewById(R.id.etPrecioProducto)
        etStockProducto = findViewById(R.id.etStockProducto)  // Cambiado de etCantidadProducto a etStockProducto
        btnGuardarProducto = findViewById(R.id.btnGuardarProducto)
        val tvFormularioTitulo: TextView = findViewById(R.id.tvFormularioTitulo)

        supermercadoId = intent.getIntExtra("supermercadoId", 0)
        productoId = intent.getIntExtra("productoId", 0).takeIf { it != 0 }

        tvFormularioTitulo.text = if (productoId != null) "Editar Producto" else "Agregar Producto"

        if (productoId != null) {
            val producto = controlador.listarProductosPorSupermercado(supermercadoId).find { it.id == productoId }
            producto?.let {
                etNombreProducto.setText(it.nombre)
                etPrecioProducto.setText(it.precio.toString())
                etStockProducto.setText(it.cantidad.toString())
            }
        }

        btnGuardarProducto.setOnClickListener {
            guardarProducto()
        }
    }

    private fun guardarProducto() {
        val nombre = etNombreProducto.text.toString()
        val precio = etPrecioProducto.text.toString().toDoubleOrNull()
        val cantidad = etStockProducto.text.toString().toIntOrNull()

        if (nombre.isNotEmpty() && precio != null && cantidad != null) {
            if (productoId != null) {
                controlador.actualizarProducto(
                    Producto(productoId!!, nombre, precio, cantidad)
                )
                Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
            } else {
                controlador.crearProducto(supermercadoId, Producto(0, nombre, precio, cantidad))
                Toast.makeText(this, "Producto creado", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
