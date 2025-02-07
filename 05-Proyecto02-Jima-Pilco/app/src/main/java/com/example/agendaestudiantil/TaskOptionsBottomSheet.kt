package com.example.agendaestudiantil

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TaskOptionsBottomSheet(
    private val task: Task,
    private val context: Context,
    private val onTaskUpdated: () -> Unit // Callback para actualizar la lista en MainActivity
) : BottomSheetDialogFragment() {

    private lateinit var taskController: TaskController

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_task_options, null)
        dialog.setContentView(view)

        taskController = TaskController(context)

        val titleTextView: TextView = view.findViewById(R.id.taskTitleTextView)
        val editButton: Button = view.findViewById(R.id.btnEditTask)
        val deleteButton: Button = view.findViewById(R.id.btnDeleteTask)
        val locationButton: Button = view.findViewById(R.id.btnViewLocation)

        titleTextView.text = task.title

        // Opción para editar tarea
        editButton.setOnClickListener {
            dismiss()
            openEditTaskScreen()
        }

        // Opción para eliminar tarea
        deleteButton.setOnClickListener {
            deleteTask()
        }

        // Opción para ver ubicación de la tarea
        locationButton.setOnClickListener {
            openMap()
        }

        return dialog
    }

    /**
     * Abre `EditTaskActivity.kt` para editar la tarea seleccionada.
     */
    private fun openEditTaskScreen() {
        val intent = Intent(context, EditTaskActivity::class.java)
        intent.putExtra("taskId", task.id)
        context.startActivity(intent)
    }

    /**
     * Elimina la tarea y actualiza la lista en `MainActivity.kt`.
     */
    private fun deleteTask() {
        val success = taskController.deleteTask(task.id ?: -1)
        if (success) {
            Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
            onTaskUpdated.invoke() // Llama al callback para actualizar la lista en MainActivity
            dismiss()
        } else {
            Toast.makeText(context, "Error al eliminar tarea", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Abre `MapActivity.kt` para visualizar la ubicación de la tarea.
     */
    private fun openMap() {
        if (task.location.isNullOrEmpty()) {
            Toast.makeText(context, "No hay ubicación asignada", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(context, MapActivity::class.java)
        intent.putExtra("taskId", task.id)
        intent.putExtra("location", task.location)
        context.startActivity(intent)
    }
}
