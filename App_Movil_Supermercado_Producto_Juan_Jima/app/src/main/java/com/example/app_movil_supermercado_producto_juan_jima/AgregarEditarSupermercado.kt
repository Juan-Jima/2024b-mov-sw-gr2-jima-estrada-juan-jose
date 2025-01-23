package com.example.app_movil_supermercado_producto_juan_jima

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ActivityAgregarEditarSupermercado : AppCompatActivity() {
    private lateinit var controlador: Controlador
    private lateinit var etNombreSupermercado: EditText
    private lateinit var etDireccionSupermercado: EditText
    private lateinit var btnGuardarSupermercado: Button
    private var supermercadoId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_editar_supermercado)

        controlador = Controlador(this)
        etNombreSupermercado = findViewById(R.id.etNombreSupermercado)
        etDireccionSupermercado = findViewById(R.id.etDireccionSupermercado)
        btnGuardarSupermercado = findViewById(R.id.btnGuardarSupermercado)
        val tvFormularioTitulo: TextView = findViewById(R.id.tvFormularioTitulo)

        supermercadoId = intent.getIntExtra("supermercadoId", 0).takeIf { it != 0 }

        tvFormularioTitulo.text = if (supermercadoId != null) "Editar Supermercado" else "Agregar Supermercado"

        if (supermercadoId != null) {
            val supermercado = controlador.listarSupermercados().find { it.id == supermercadoId }
            supermercado?.let {
                etNombreSupermercado.setText(it.nombre)
                etDireccionSupermercado.setText(it.direccion)
            }
        }

        btnGuardarSupermercado.setOnClickListener {
            guardarSupermercado()
        }
    }

    private fun guardarSupermercado() {
        val nombre = etNombreSupermercado.text.toString()
        val direccion = etDireccionSupermercado.text.toString()

        if (nombre.isNotEmpty() && direccion.isNotEmpty()) {
            if (supermercadoId != null) {
                controlador.actualizarSupermercado(
                    Supermercado(supermercadoId!!, nombre, direccion, true, 0.0)
                )
                Toast.makeText(this, "Supermercado actualizado", Toast.LENGTH_SHORT).show()
            } else {
                val nuevoId = (controlador.listarSupermercados().maxOfOrNull { it.id } ?: 0) + 1
                controlador.crearSupermercado(Supermercado(nuevoId, nombre, direccion, true, 0.0))
                Toast.makeText(this, "Supermercado creado", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
