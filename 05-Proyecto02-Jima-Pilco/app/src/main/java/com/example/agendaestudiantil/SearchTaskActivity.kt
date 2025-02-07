package com.example.agendaestudiantil

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchTaskActivity : AppCompatActivity() {

    private lateinit var taskController: TaskController
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var searchInput: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_task)

        // Inicializar controlador de tareas
        taskController = TaskController(this)

        // Inicializar vistas
        searchInput = findViewById(R.id.searchInput)
        recyclerView = findViewById(R.id.recyclerView)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskController.getAllTasks().toMutableList(), taskController)
        recyclerView.adapter = taskAdapter

        // Agregar evento de búsqueda en tiempo real
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterTasks(s.toString())
            }
        })

        val backButton: Button = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }

    }

    /**
     * Filtra la lista de tareas en función del texto ingresado en el `EditText`.
     */
    private fun filterTasks(query: String) {
        val filteredTasks: List<Task> = taskController.searchTasks(query)

        if (filteredTasks.isEmpty() && query.isNotEmpty()) {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
        }

        taskAdapter.updateList(filteredTasks.toMutableList())
    }
}
