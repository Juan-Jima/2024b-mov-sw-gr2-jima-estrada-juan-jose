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

        val defaultLocation = LatLng(-0.1807, -78.4688)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        val taskLocation = intent.getStringExtra("location")
        if (taskLocation != null) {
            val latLng = taskLocation.split(",").map { it.toDouble() }
            val markerPosition = LatLng(latLng[0], latLng[1])
            mMap.addMarker(MarkerOptions().position(markerPosition).title("Ubicación guardada"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 15f))
        }

        // Permitir seleccionar ubicación
        mMap.setOnMapClickListener { latLng ->
            selectedMarker?.remove()
            selectedMarker = mMap.addMarker(MarkerOptions().position(latLng).title("Ubicación seleccionada"))

            val latLngString = "${latLng.latitude},${latLng.longitude}"
            val resultIntent = Intent().apply {
                putExtra("selectedLocation", latLngString)
            }
            setResult(RESULT_OK, resultIntent)
            Toast.makeText(this, "Ubicación guardada", Toast.LENGTH_SHORT).show()
            finish() // ✅ Cierra la actividad automáticamente
        }
    }

}
