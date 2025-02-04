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
                etStockProducto.setText(it.stock.toString()) // Usamos 'stock' en lugar de 'cantidad'
            }
        }

        btnGuardarProducto.setOnClickListener {
            guardarProducto()
        }
    }

    private fun guardarProducto() {
        val nombre = etNombreProducto.text.toString()
        val precio = etPrecioProducto.text.toString().toDoubleOrNull()
        val stock = etStockProducto.text.toString().toIntOrNull() // Cambiado de 'cantidad' a 'stock'

        if (nombre.isNotEmpty() && precio != null && stock != null) {
            if (productoId != null) {
                // Pasar 'supermercadoId' y actualizar el producto
                controlador.actualizarProducto(
                    Producto(productoId!!, nombre, precio, stock, supermercadoId) // Asegúrate de pasar el supermercadoId
                )
                Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
            } else {
                // Pasar 'supermercadoId' y crear el producto
                controlador.crearProducto(supermercadoId, Producto(0, nombre, precio, stock, supermercadoId)) // Asegúrate de pasar el supermercadoId
                Toast.makeText(this, "Producto creado", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
