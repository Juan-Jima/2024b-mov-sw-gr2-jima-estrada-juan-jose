package com.example.app_movil_supermercado_producto_juan_jima

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class ActivityAgregarEditarSupermercado : AppCompatActivity() {
    private lateinit var controlador: Controlador
    private lateinit var etNombreSupermercado: EditText
    private lateinit var etDireccionSupermercado: EditText
    private lateinit var etUbicacionSupermercado: EditText
    private lateinit var btnGuardarSupermercado: Button
    private lateinit var btnObtenerUbicacion: Button // Botón para obtener ubicación
    private var supermercadoId: Int? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_editar_supermercado)

        controlador = Controlador(this)
        etNombreSupermercado = findViewById(R.id.etNombreSupermercado)
        etDireccionSupermercado = findViewById(R.id.etDireccionSupermercado)
        etUbicacionSupermercado = findViewById(R.id.etUbicacionSupermercado)
        btnGuardarSupermercado = findViewById(R.id.btnGuardarSupermercado)
        btnObtenerUbicacion = findViewById(R.id.btnObtenerUbicacion) // Inicializamos el botón
        val tvFormularioTitulo: TextView = findViewById(R.id.tvFormularioTitulo)

        // Inicializa el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Recupera el ID del supermercado si se está editando
        supermercadoId = intent.getIntExtra("supermercadoId", 0).takeIf { it != 0 }

        tvFormularioTitulo.text = if (supermercadoId != null) "Editar Supermercado" else "Agregar Supermercado"

        // Si estamos editando, muestra los datos del supermercado
        if (supermercadoId != null) {
            val supermercado = controlador.listarSupermercados().find { it.id == supermercadoId }
            supermercado?.let {
                etNombreSupermercado.setText(it.nombre)
                etDireccionSupermercado.setText(it.direccion)
                etUbicacionSupermercado.setText(it.ubicacion)
            }
        }

        // Guardar supermercado
        btnGuardarSupermercado.setOnClickListener {
            guardarSupermercado()
        }

        // Obtener ubicación al presionar el botón
        btnObtenerUbicacion.setOnClickListener {
            obtenerUbicacionGPS()
        }
    }

    // Método para obtener la ubicación
    private fun obtenerUbicacionGPS() {
        // Comprobación de permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Si no tiene permisos, solicita los permisos
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        // Obtener la última ubicación conocida
        fusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener<Location> { location ->
            if (location != null) {
                // Muestra la ubicación (latitud y longitud)
                val ubicacion = "Lat: ${location.latitude}, Long: ${location.longitude}"
                etUbicacionSupermercado.setText(ubicacion) // Establece la ubicación en el campo de texto
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Método para guardar el supermercado
    private fun guardarSupermercado() {
        val nombre = etNombreSupermercado.text.toString()
        val direccion = etDireccionSupermercado.text.toString()
        val ubicacion = etUbicacionSupermercado.text.toString()

        // Verifica que los campos no estén vacíos
        if (nombre.isNotEmpty() && direccion.isNotEmpty() && ubicacion.isNotEmpty()) {
            if (supermercadoId != null) {
                // Si es una edición, actualiza el supermercado
                controlador.actualizarSupermercado(
                    Supermercado(supermercadoId!!, nombre, direccion, true, 0.0, ubicacion)
                )
                Toast.makeText(this, "Supermercado actualizado", Toast.LENGTH_SHORT).show()
            } else {
                // Si es una creación, agrega un nuevo supermercado
                val nuevoId = (controlador.listarSupermercados().maxOfOrNull { it.id } ?: 0) + 1
                controlador.crearSupermercado(Supermercado(nuevoId, nombre, direccion, true, 0.0, ubicacion))
                Toast.makeText(this, "Supermercado creado", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    // Métodos para manejar la solicitud de permisos (si es necesario)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, obtenemos la ubicación
                obtenerUbicacionGPS()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
