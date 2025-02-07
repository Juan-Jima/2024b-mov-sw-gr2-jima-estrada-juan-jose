package com.example.agendaestudiantil

import android.content.Intent
import android.location.Geocoder
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException
import java.util.Locale

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var selectedMarker: Marker? = null
    private var selectedLocation: LatLng? = null
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // ✅ CORREGIR EL CASTING: Usar FloatingActionButton en vez de Button
        val saveLocationButton: FloatingActionButton = findViewById(R.id.btnConfirmLocation)
        saveLocationButton.setOnClickListener {
            saveSelectedLocation()
        }

        val closeMapButton: Button = findViewById(R.id.btnCloseMap)
        closeMapButton.setOnClickListener {
            finish()
        }

        // Obtener el fragmento del mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Obtener el ID de la tarea desde el Intent
        taskId = intent.getIntExtra("taskId", -1)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Habilitar controles de zoom
        mMap.uiSettings.isZoomControlsEnabled = true

        // Ubicación por defecto (Quito, Ecuador)
        val defaultLocation = LatLng(-0.1807, -78.4688)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        // Verificar si la tarea ya tiene una ubicación guardada
        val taskLocation = intent.getStringExtra("location")
        if (!taskLocation.isNullOrEmpty()) {
            val latLng = taskLocation.split(",").map { it.toDouble() }
            val markerPosition = LatLng(latLng[0], latLng[1])
            selectedMarker = mMap.addMarker(
                MarkerOptions()
                    .position(markerPosition)
                    .title(getLocationName(markerPosition))
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 15f))
        }

        // Evento al hacer clic en el mapa para agregar un marcador
        mMap.setOnMapClickListener { latLng ->
            selectedMarker?.remove() // Eliminar marcador anterior

            // Obtener el nombre de la ubicación seleccionada
            val locationName = getLocationName(latLng)

            // Agregar el nuevo marcador con el nombre
            selectedMarker = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(locationName)
            )

            selectedLocation = latLng // Guardar la ubicación seleccionada
        }
    }

    /**
     * Obtiene el nombre de la ubicación basada en latitud y longitud.
     */
    private fun getLocationName(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                addresses[0].getAddressLine(0) // Devuelve la dirección completa
            } else {
                "Ubicación desconocida"
            }
        } catch (e: IOException) {
            "Ubicación no disponible"
        }
    }

    /**
     * Guarda la ubicación seleccionada y la envía de regreso a `MainActivity`.
     */
    private fun saveSelectedLocation() {
        if (selectedLocation != null) {
            val latLngString = "${selectedLocation!!.latitude},${selectedLocation!!.longitude}"
            val resultIntent = Intent().apply {
                putExtra("selectedLocation", latLngString)
                putExtra("taskId", taskId)
            }
            setResult(RESULT_OK, resultIntent)
            Toast.makeText(this, "Ubicación guardada", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Seleccione una ubicación antes de guardar", Toast.LENGTH_SHORT).show()
        }
    }
}
