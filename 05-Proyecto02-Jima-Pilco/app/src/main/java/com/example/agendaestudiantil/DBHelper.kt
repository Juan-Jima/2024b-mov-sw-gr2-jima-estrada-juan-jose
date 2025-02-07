package com.example.agendaestudiantil

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.agendaestudiantil.Task

class DBHelper(context: Context) : SQLiteOpenHelper(context, "TasksDB", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                title TEXT NOT NULL, 
                description TEXT, 
                date TEXT NOT NULL, 
                location TEXT DEFAULT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE tasks ADD COLUMN location TEXT DEFAULT NULL")
        }
    }

    /**
     * Insertar una nueva tarea en la base de datos.
     */
    fun insertTask(task: Task): Boolean {
        val db = writableDatabase
        return try {
            db.beginTransaction()
            val values = ContentValues().apply {
                put("title", task.title)
                put("description", task.description)
                put("date", task.date)
                put("location", task.location)
            }
            val success = db.insert("tasks", null, values) > 0
            db.setTransactionSuccessful()
            success
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    /**
     * Obtener todas las tareas ordenadas por fecha.
     */
    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM tasks ORDER BY date ASC", null)

        cursor?.use {
            while (it.moveToNext()) {
                val task = Task(
                    id = it.getInt(0),
                    title = it.getString(1),
                    description = it.getString(2),
                    date = it.getString(3),
                    location = it.getString(4) // Recuperar ubicación
                )
                taskList.add(task)
            }
        }
        db.close()
        return taskList
    }

    /**
     * Buscar tareas por título.
     */
    fun searchTasks(query: String): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM tasks WHERE title LIKE ? ORDER BY date ASC", arrayOf("%$query%"))

        cursor?.use {
            while (it.moveToNext()) {
                val task = Task(
                    id = it.getInt(0),
                    title = it.getString(1),
                    description = it.getString(2),
                    date = it.getString(3),
                    location = it.getString(4) // Recuperar ubicación
                )
                taskList.add(task)
            }
        }
        db.close()
        return taskList
    }

    /**
     * Eliminar una tarea por su ID.
     */
    fun deleteTask(taskId: Int): Boolean {
        val db = writableDatabase
        return try {
            db.beginTransaction()
            val success = db.delete("tasks", "id = ?", arrayOf(taskId.toString())) > 0
            db.setTransactionSuccessful()
            success
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    /**
     * Actualizar una tarea en la base de datos.
     */
    fun updateTask(task: Task): Boolean {
        val db = writableDatabase
        return try {
            db.beginTransaction()
            val values = ContentValues().apply {
                put("title", task.title)
                put("description", task.description)
                put("date", task.date)
                put("location", task.location)
            }
            val success = db.update("tasks", values, "id = ?", arrayOf(task.id.toString())) > 0
            db.setTransactionSuccessful()
            success
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    /**
     * Actualizar solo la ubicación de una tarea.
     */
    fun updateTaskLocation(taskId: Int, location: String): Boolean {
        val db = writableDatabase
        return try {
            db.beginTransaction()
            val values = ContentValues().apply {
                put("location", location)
            }
            val success = db.update("tasks", values, "id = ?", arrayOf(taskId.toString())) > 0
            db.setTransactionSuccessful()
            success
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}
