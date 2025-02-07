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

class EditTaskActivity : AppCompatActivity() {

    private lateinit var taskController: TaskController
    private lateinit var titleInput: EditText
    private lateinit var descInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var locationButton: Button
    private lateinit var updateButton: Button

    private var selectedLocation: String? = null
    private var taskId: Int = -1
    private var currentTask: Task? = null

    // Lanzador de actividad para el mapa
    private val mapActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedLocation = result.data?.getStringExtra("selectedLocation")
                Toast.makeText(this, "Ubicación actualizada", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        // Inicializar controlador
        taskController = TaskController(this)

        // Obtener el ID de la tarea a editar
        taskId = intent.getIntExtra("taskId", -1)
        if (taskId == -1) {
            Toast.makeText(this, "Error al cargar la tarea", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inicializar vistas
        titleInput = findViewById(R.id.editTitleInput)
        descInput = findViewById(R.id.editDescInput)
        dateInput = findViewById(R.id.editDateInput)
        locationButton = findViewById(R.id.btnUpdateLocation)
        updateButton = findViewById(R.id.btnUpdateTask)

        // Cargar los datos de la tarea
        loadTaskData()

        // Configurar selector de fecha
        dateInput.setOnClickListener { showDatePicker() }

        // Botón para cambiar la ubicación de la tarea
        locationButton.setOnClickListener { openMap() }

        // Botón para actualizar la tarea
        updateButton.setOnClickListener { updateTask() }
    }

    /**
     * Carga los datos de la tarea en los campos de edición.
     */
    private fun loadTaskData() {
        currentTask = taskController.getAllTasks().find { it.id == taskId }

        if (currentTask == null) {
            Toast.makeText(this, "No se encontró la tarea", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        titleInput.setText(currentTask!!.title)
        descInput.setText(currentTask!!.description)
        dateInput.setText(currentTask!!.date)
        selectedLocation = currentTask!!.location
    }

    /**
     * Muestra un `DatePickerDialog` para seleccionar la nueva fecha de la tarea.
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
     * Abre `MapActivity.kt` para cambiar la ubicación de la tarea.
     */
    private fun openMap() {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("taskId", taskId)
        mapActivityLauncher.launch(intent)
    }

    /**
     * Guarda los cambios en la tarea y actualiza la base de datos.
     */
    private fun updateTask() {
        val title = titleInput.text.toString().trim()
        val desc = descInput.text.toString().trim()
        val date = dateInput.text.toString().trim()

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese título y fecha", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedTask = Task(
            id = taskId,
            title = title,
            description = desc,
            date = date,
            location = selectedLocation
        )

        val success = taskController.updateTask(updatedTask)

        if (success) {
            Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show()
            finish() // Cierra la actividad y vuelve a `MainActivity.kt`
        } else {
            Toast.makeText(this, "Error al actualizar la tarea", Toast.LENGTH_SHORT).show()
        }
    }
}
