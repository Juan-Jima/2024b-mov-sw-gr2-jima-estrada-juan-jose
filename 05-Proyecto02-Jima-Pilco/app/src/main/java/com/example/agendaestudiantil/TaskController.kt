package com.example.agendaestudiantil

import android.content.Context

class TaskController(context: Context) {

    private val dbHelper = DBHelper(context)

    /**
     * Agrega una nueva tarea a la base de datos
     */
    fun addTask(title: String, description: String, date: String): Boolean {
        if (title.isNotEmpty() && date.isNotEmpty()) {
            val task = Task(title = title, description = description, date = date)
            return dbHelper.insertTask(task)
        }
        return false
    }

    /**
     * Obtiene todas las tareas de la base de datos
     */
    fun getAllTasks(): MutableList<Task> {
        return dbHelper.getAllTasks().toMutableList()
    }

    /**
     * Busca tareas por título
     */
    fun searchTasks(query: String): MutableList<Task> {
        return dbHelper.searchTasks(query).toMutableList()
    }

    /**
     * Actualiza la ubicación de una tarea
     */
    fun updateTaskLocation(task: Task, location: String) {
        task.location = location
        dbHelper.updateTask(task)
    }
}
