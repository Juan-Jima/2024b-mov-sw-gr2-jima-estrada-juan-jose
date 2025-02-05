package com.example.agendaestudiantil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.app.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var taskAdapter: TaskAdapter
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val addButton: Button = findViewById(R.id.addButton)
        val titleInput: EditText = findViewById(R.id.titleInput)
        val descInput: EditText = findViewById(R.id.descInput)
        val dateInput: EditText = findViewById(R.id.dateInput)
        val searchInput: EditText = findViewById(R.id.searchInput)
        val mapButton: Button = findViewById(R.id.mapButton)

        dbHelper = DBHelper(this)

        // Cargar tareas desde la base de datos
        val tasks = dbHelper.getAllTasks().toMutableList() // Convertir la lista a MutableList
        taskAdapter = TaskAdapter(tasks, dbHelper) // Inicializar con la lista mutable y el dbHelper
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, { _, year, month, day ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                dateInput.setText(dateFormat.format(selectedDate.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val desc = descInput.text.toString()
            val date = dateInput.text.toString()

            if (title.isNotEmpty() && date.isNotEmpty()) {
                val task = Task(title = title, description = desc, date = date) // Correctly passing parameters
                dbHelper.insertTask(task)
                taskAdapter.updateList(dbHelper.getAllTasks().toMutableList()) // Actualizar la lista después de agregar
                titleInput.text.clear()
                descInput.text.clear()
                dateInput.text.clear()
            } else {
                Toast.makeText(this, "Ingrese título y fecha", Toast.LENGTH_SHORT).show()
            }
        }

        searchInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                taskAdapter.filter.filter(s)
            }
        })

        mapButton.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }
}
