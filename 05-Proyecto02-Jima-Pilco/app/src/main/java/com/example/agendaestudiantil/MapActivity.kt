package com.example.agendaestudiantil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var selectedMarker: Marker? = null // Para manejar la ubicación seleccionada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtener el fragmento del mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Botón para cerrar el mapa
        val closeMapButton: Button = findViewById(R.id.closeMapButton)
        closeMapButton.setOnClickListener {
            finish() // Cierra la actividad del mapa
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Ubicación predeterminada (Quito, Ecuador)
        val defaultLocation = LatLng(-0.1807, -78.4688)
        mMap.addMarker(MarkerOptions().position(defaultLocation).title("Ubicación predeterminada"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        // Habilitar clics en el mapa para seleccionar ubicaciones
        mMap.setOnMapClickListener { latLng ->
            // Si ya hay un marcador seleccionado, lo eliminamos
            selectedMarker?.remove()

            // Agregamos un nuevo marcador en la ubicación seleccionada
            selectedMarker = mMap.addMarker(
                MarkerOptions().position(latLng).title("Ubicación seleccionada")
            )

            // Mostrar un mensaje con la latitud y longitud
            Toast.makeText(this, "Ubicación seleccionada: ${latLng.latitude}, ${latLng.longitude}", Toast.LENGTH_SHORT).show()
        }
    }
}
