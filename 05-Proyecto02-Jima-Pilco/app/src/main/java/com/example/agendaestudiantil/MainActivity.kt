package com.example.agendaestudiantil

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var taskController: TaskController
    private lateinit var taskAdapter: TaskAdapter
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var selectedTask: Task? = null

    // Lanzador de actividad para el mapa
    private val mapActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val location = result.data?.getStringExtra("selectedLocation")
                if (location != null && selectedTask != null) {
                    taskController.updateTaskLocation(selectedTask!!, location)
                    taskAdapter.updateList(taskController.getAllTasks())
                    Toast.makeText(this, "Ubicación guardada en la tarea", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskController = TaskController(this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val addButton: FloatingActionButton = findViewById(R.id.addButton)
        val titleInput: EditText = findViewById(R.id.titleInput)
        val descInput: EditText = findViewById(R.id.descInput)
        val dateInput: EditText = findViewById(R.id.dateInput)
        val searchInput: EditText = findViewById(R.id.searchInput)
        val mapButton: FloatingActionButton = findViewById(R.id.mapButton) // ✅ CORRECTO

        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskController.getAllTasks(), DBHelper(this))
        recyclerView.adapter = taskAdapter

        dateInput.setOnClickListener { showDatePicker(dateInput) }

        addButton.setOnClickListener {
            if (taskController.addTask(titleInput.text.toString(), descInput.text.toString(), dateInput.text.toString())) {
                taskAdapter.updateList(taskController.getAllTasks())
                titleInput.text.clear()
                descInput.text.clear()
                dateInput.text.clear()
                Toast.makeText(this, "Tarea añadida", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ingrese título y fecha", Toast.LENGTH_SHORT).show()
            }
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                taskAdapter.updateList(taskController.searchTasks(s.toString()))
            }
        })

        mapButton.setOnClickListener { openMap() }
    }

    private fun showDatePicker(dateInput: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(this, { _, year, month, day ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            dateInput.setText(dateFormat.format(selectedDate.time))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }

    private fun openMap() {
        val intent = Intent(this, MapActivity::class.java)
        mapActivityLauncher.launch(intent)
    }
}
