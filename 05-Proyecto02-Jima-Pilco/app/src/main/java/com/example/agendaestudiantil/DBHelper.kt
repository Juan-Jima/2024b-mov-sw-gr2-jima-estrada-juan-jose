package com.example.agendaestudiantil

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "TasksDB", null, 1) {

    // Método onCreate para crear la tabla
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, date TEXT)")
    }

    // Método onUpgrade para manejar cambios de versión de la base de datos
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    // Insertar una nueva tarea en la base de datos
    fun insertTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("title", task.title)
        values.put("description", task.description)
        values.put("date", task.date)
        db.insert("tasks", null, values)
    }

    // Obtener todas las tareas de la base de datos
    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        if (cursor.moveToFirst()) {
            do {
                val task = Task(
                    cursor.getInt(0), // id (índice 0)
                    cursor.getString(1), // title (índice 1)
                    cursor.getString(2), // description (índice 2)
                    cursor.getString(3)  // date (índice 3)
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return taskList
    }

    // Eliminar una tarea por su id
    fun deleteTask(task: Task) {
        val db = writableDatabase
        db.delete("tasks", "id = ?", arrayOf(task.id.toString()))
        db.close()
    }

    // Actualizar una tarea en la base de datos
    fun updateTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("title", task.title)
        values.put("description", task.description)
        values.put("date", task.date)
        db.update("tasks", values, "id = ?", arrayOf(task.id.toString()))
        db.close()
    }
}
