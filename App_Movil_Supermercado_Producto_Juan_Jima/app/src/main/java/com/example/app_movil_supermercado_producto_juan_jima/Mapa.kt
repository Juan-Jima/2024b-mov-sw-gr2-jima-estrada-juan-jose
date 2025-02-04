package com.example.app_movil_supermercado_producto_juan_jima

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Mapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var controlador: Controlador
    private var supermercadoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        controlador = Controlador(this)
        supermercadoId = intent.getIntExtra("supermercadoId", -1)

        if (supermercadoId == -1) {
            Toast.makeText(this, "Error: Supermercado no encontrado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Verificar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permisos si no se tienen
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        // Obtener el fragmento del mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Habilitar controles de zoom y brújula
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        // Obtener el supermercado por ID
        val supermercado = controlador.listarSupermercados().find { it.id == supermercadoId }

        supermercado?.let {
            // Verificar si la dirección tiene coordenadas (latitud,longitud)
            val ubicacion = it.direccion.split(",")
            if (ubicacion.size == 2) {
                try {
                    // Convertir las coordenadas a LatLng
                    val latitud = ubicacion[0].trim().toDouble()
                    val longitud = ubicacion[1].trim().toDouble()

                    // Imprimir las coordenadas en el log para verificar
                    Log.d("Mapa", "Coordenadas obtenidas: Latitud: $latitud, Longitud: $longitud")

                    // Verificar que las coordenadas sean válidas (no 0.0)
                    if (latitud != 0.0 && longitud != 0.0) {
                        val position = LatLng(latitud, longitud)

                        // Agregar el marcador
                        mMap.addMarker(MarkerOptions().position(position).title(it.nombre))

                        // Mover la cámara al marcador
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
                    } else {
                        Toast.makeText(this, "Ubicación inválida para este supermercado", Toast.LENGTH_SHORT).show()
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 2f))
                    }
                } catch (e: Exception) {
                    // Si hay algún error al convertir las coordenadas
                    Log.e("Mapa", "Error al convertir las coordenadas", e)
                    Toast.makeText(this, "Error al procesar la ubicación", Toast.LENGTH_SHORT).show()
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 2f))
                }
            } else {
                Toast.makeText(this, "Ubicación no disponible para este supermercado", Toast.LENGTH_SHORT).show()
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 2f))
            }
        } ?: run {
            Toast.makeText(this, "No se encontró información del supermercado", Toast.LENGTH_SHORT).show()
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 2f))
        }
    }
}
