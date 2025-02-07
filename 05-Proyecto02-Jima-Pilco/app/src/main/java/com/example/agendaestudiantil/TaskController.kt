package com.example.agendaestudiantil

import android.content.Context

class TaskController(context: Context) {

    private val dbHelper = DBHelper(context)

    /**
     * Agrega una nueva tarea a la base de datos.
     * Puede incluir ubicación si se proporciona.
     */
    fun addTask(title: String, description: String, date: String, location: String? = null): Boolean {
        if (title.isNotEmpty() && date.isNotEmpty()) {
            val task = Task(title = title, description = description, date = date, location = location)
            return dbHelper.insertTask(task)
        }
        return false
    }

    /**
     * Obtiene todas las tareas de la base de datos ordenadas por fecha.
     */
    fun getAllTasks(): List<Task> {
        return dbHelper.getAllTasks()
    }

    /**
     * Busca tareas por título.
     */
    fun searchTasks(query: String): List<Task> {
        return dbHelper.searchTasks(query)
    }

    /**
     * Actualiza los datos de una tarea.
     */
    fun updateTask(task: Task): Boolean {
        return dbHelper.updateTask(task)
    }

    /**
     * Actualiza solo la ubicación de una tarea en la base de datos.
     */
    fun updateTaskLocation(taskId: Int, location: String): Boolean {
        return dbHelper.updateTaskLocation(taskId, location)
    }

    /**
     * Elimina una tarea por su ID.
     */
    fun deleteTask(taskId: Int): Boolean {
        return dbHelper.deleteTask(taskId)
    }
}
