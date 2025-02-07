package com.example.agendaestudiantil

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "TasksDB", null, 1) {

    // Método onCreate para crear la tabla
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE tasks (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "description TEXT, " +
                    "date TEXT NOT NULL)"
        )
    }

    // Método onUpgrade para manejar cambios en la base de datos
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks") // Borra la tabla si existe
        onCreate(db) // Crea nuevamente la tabla
    }

    // Insertar una nueva tarea en la base de datos
    fun insertTask(task: Task): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("title", task.title)
                put("description", task.description)
                put("date", task.date)
            }
            db.insert("tasks", null, values) > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    // Obtener todas las tareas de la base de datos
    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT * FROM tasks ORDER BY date ASC", null)
            while (cursor.moveToNext()) {
                val task = Task(
                    id = cursor.getInt(0),
                    title = cursor.getString(1),
                    description = cursor.getString(2),
                    date = cursor.getString(3)
                )
                taskList.add(task)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
        return taskList
    }

    // Buscar tareas por título (para la funcionalidad de búsqueda)
    fun searchTasks(query: String): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT * FROM tasks WHERE title LIKE ? ORDER BY date ASC", arrayOf("%$query%"))
            while (cursor.moveToNext()) {
                val task = Task(
                    id = cursor.getInt(0),
                    title = cursor.getString(1),
                    description = cursor.getString(2),
                    date = cursor.getString(3)
                )
                taskList.add(task)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
        return taskList
    }

    // Eliminar una tarea por su id
    fun deleteTask(taskId: Int): Boolean {
        val db = writableDatabase
        return try {
            db.delete("tasks", "id = ?", arrayOf(taskId.toString())) > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    // Actualizar una tarea en la base de datos
    fun updateTask(task: Task): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("title", task.title)
                put("description", task.description)
                put("date", task.date)
            }
            db.update("tasks", values, "id = ?", arrayOf(task.id.toString())) > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }
}
