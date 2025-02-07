package com.example.agendaestudiantil

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var taskAdapter: TaskAdapter
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de vistas
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val addButton: Button = findViewById(R.id.addButton)
        val titleInput: EditText = findViewById(R.id.titleInput)
        val descInput: EditText = findViewById(R.id.descInput)
        val dateInput: EditText = findViewById(R.id.dateInput)
        val searchInput: EditText = findViewById(R.id.searchInput)
        val mapButton: Button = findViewById(R.id.mapButton)

        // Inicializar Base de Datos
        dbHelper = DBHelper(this)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(dbHelper.getAllTasks().toMutableList(), dbHelper)
        recyclerView.adapter = taskAdapter

        // Seleccionar fecha con DatePickerDialog
        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, { _, year, month, day ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                dateInput.setText(dateFormat.format(selectedDate.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        // Agregar una nueva tarea
        addButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val desc = descInput.text.toString().trim()
            val date = dateInput.text.toString().trim()

            if (title.isNotEmpty() && date.isNotEmpty()) {
                val task = Task(title = title, description = desc, date = date)
                if (dbHelper.insertTask(task)) {
                    taskAdapter.updateList(dbHelper.getAllTasks().toMutableList()) // Actualizar la lista
                    titleInput.text.clear()
                    descInput.text.clear()
                    dateInput.text.clear()
                    Toast.makeText(this, "Tarea añadida", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al añadir la tarea", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingrese título y fecha", Toast.LENGTH_SHORT).show()
            }
        }

        // Búsqueda en tiempo real
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredTasks = dbHelper.searchTasks(s.toString())
                taskAdapter.updateList(filteredTasks.toMutableList())
            }
        })

        // Botón para ver el mapa
        mapButton.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }
}
