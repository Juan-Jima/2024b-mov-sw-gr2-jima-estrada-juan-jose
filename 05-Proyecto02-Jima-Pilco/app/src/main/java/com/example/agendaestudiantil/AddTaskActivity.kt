package com.example.agendaestudiantil

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskController: TaskController
    private lateinit var titleInput: EditText
    private lateinit var descInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var locationButton: Button
    private lateinit var saveButton: Button

    private var selectedLocation: String? = null // Para almacenar la ubicación seleccionada

    // Lanzador de actividad para el mapa
    private val mapActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedLocation = result.data?.getStringExtra("selectedLocation")
                Toast.makeText(this, "Ubicación asignada", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // Inicializar controlador
        taskController = TaskController(this)

        // Inicializar vistas
        titleInput = findViewById(R.id.titleInput)
        descInput = findViewById(R.id.descInput)
        dateInput = findViewById(R.id.dateInput)
        locationButton = findViewById(R.id.locationButton)
        saveButton = findViewById(R.id.saveButton)

        // Configurar selector de fecha
        dateInput.setOnClickListener { showDatePicker() }

        // Botón para seleccionar ubicación en el mapa
        locationButton.setOnClickListener { openMap() }

        // Botón para guardar la tarea
        saveButton.setOnClickListener { saveTask() }
    }

    /**
     * Muestra un `DatePickerDialog` para seleccionar la fecha de la tarea.
     */
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(this, { _, year, month, day ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateInput.setText(dateFormat.format(selectedDate.time))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }

    /**
     * Abre `MapActivity.kt` para seleccionar una ubicación.
     */
    private fun openMap() {
        val intent = Intent(this, MapActivity::class.java)
        mapActivityLauncher.launch(intent)
    }

    /**
     * Guarda la tarea en la base de datos y regresa a `MainActivity.kt`.
     */
    private fun saveTask() {
        val title = titleInput.text.toString().trim()
        val desc = descInput.text.toString().trim()
        val date = dateInput.text.toString().trim()

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese título y fecha", Toast.LENGTH_SHORT).show()
            return
        }

        val success = taskController.addTask(title, desc, date, selectedLocation)

        if (success) {
            Toast.makeText(this, "Tarea guardada exitosamente", Toast.LENGTH_SHORT).show()
            finish() // Cierra la actividad y vuelve a `MainActivity.kt`
        } else {
            Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show()
        }
    }
}
