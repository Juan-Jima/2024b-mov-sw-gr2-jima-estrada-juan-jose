package com.example.agendaestudiantil

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var taskController: TaskController
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var searchInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el controlador de tareas
        taskController = TaskController(this)

        // Configurar RecyclerView, botones y búsqueda
        setupRecyclerView()
        setupButtons()
        setupSearch()
    }

    /**
     * Configura el RecyclerView y su adaptador.
     */
    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskController.getAllTasks().toMutableList(), taskController)
        recyclerView.adapter = taskAdapter

        // Evento de pulsación larga para mostrar opciones (editar, eliminar, ver ubicación)
        taskAdapter.setOnTaskLongClickListener { task ->
            showTaskOptions(task)
        }
    }

    /**
     * Configura los botones flotantes de agregar y buscar.
     */
    private fun setupButtons() {
        val addButton: FloatingActionButton = findViewById(R.id.addButton)
        val searchButton: FloatingActionButton = findViewById(R.id.searchButton)

        addButton.setOnClickListener { openAddTask() }
        searchButton.setOnClickListener { openSearchTask() }
    }

    /**
     * Configura la búsqueda de tareas en tiempo real.
     */
    private fun setupSearch() {
        searchInput = findViewById(R.id.searchInput)
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredTasks = taskController.searchTasks(s.toString())
                taskAdapter.updateList(filteredTasks.toMutableList())
            }
        })
    }

    /**
     * Abre `AddTaskActivity.kt` para agregar una nueva tarea.
     */
    private fun openAddTask() {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivity(intent)
    }

    /**
     * Abre `SearchTaskActivity.kt` para búsqueda avanzada de tareas.
     */
    private fun openSearchTask() {
        val intent = Intent(this, SearchTaskActivity::class.java)
        startActivity(intent)
    }

    /**
     * Muestra un `BottomSheetDialog` con opciones de tarea al hacer una pulsación larga.
     */
    private fun showTaskOptions(task: Task) {
        val bottomSheetDialog = TaskOptionsBottomSheet(
            task = task,
            context = this,
            onTaskUpdated = {
                taskAdapter.updateList(taskController.getAllTasks().toMutableList())
                Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show()
            }
        )
        bottomSheetDialog.show(supportFragmentManager, "TaskOptionsBottomSheet")
    }


}
