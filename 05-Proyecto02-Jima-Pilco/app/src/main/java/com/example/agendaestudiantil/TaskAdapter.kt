package com.example.agendaestudiantil

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.view.menu.MenuBuilder
import android.widget.PopupMenu
import java.util.*

class TaskAdapter(private var taskList: MutableList<Task>, private val dbHelper: DBHelper) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(), Filterable {
    private var filteredTaskList: MutableList<Task> = taskList

    fun updateList(newList: MutableList<Task>) {
        taskList = newList
        filteredTaskList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(filteredTaskList[position])
    }

    override fun getItemCount(): Int = filteredTaskList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase(Locale.getDefault())
                filteredTaskList = if (query.isNullOrEmpty()) {
                    taskList
                } else {
                    taskList.filter { it.title.lowercase(Locale.getDefault()).contains(query) }.toMutableList()
                }
                val results = FilterResults()
                results.values = filteredTaskList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredTaskList = results?.values as MutableList<Task>
                notifyDataSetChanged()
            }
        }
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleView: TextView = view.findViewById(R.id.taskTitle)
        private val descView: TextView = view.findViewById(R.id.taskDesc)
        private val dateView: TextView = view.findViewById(R.id.taskDate)

        init {
            // Al hacer clic en cualquier parte del LinearLayout, se muestra el menú con opciones
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = filteredTaskList[position]
                    showOptionsMenu(task)
                }
            }
        }

        fun bind(task: Task) {
            titleView.text = task.title
            descView.text = task.description
            dateView.text = task.date
        }

        private fun showOptionsMenu(task: Task) {
            val context = itemView.context
            val popupMenu = PopupMenu(context, itemView)

            // Cargar las opciones del menú
            popupMenu.menuInflater.inflate(R.menu.task_options_menu, popupMenu.menu)

            // Manejo de la opción de editar
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        showEditDialog(task)
                        true
                    }
                    R.id.menu_delete -> {
                        dbHelper.deleteTask(task)
                        taskList.remove(task)
                        updateList(taskList)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show() // Mostrar el menú emergente
        }

        private fun showEditDialog(task: Task) {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Editar Tarea")
            val view = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_edit_task, null)

            val titleInput = view.findViewById<EditText>(R.id.editTitleInput)
            val descInput = view.findViewById<EditText>(R.id.editDescInput)
            val dateInput = view.findViewById<EditText>(R.id.editDateInput)

            titleInput.setText(task.title)
            descInput.setText(task.description)
            dateInput.setText(task.date)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnUpdate).setOnClickListener {
                task.title = titleInput.text.toString()
                task.description = descInput.text.toString()
                task.date = dateInput.text.toString()
                dbHelper.updateTask(task)
                updateList(dbHelper.getAllTasks().toMutableList())
                dialog.dismiss()  // Cierra el diálogo después de actualizar
            }

            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()  // Cierra el diálogo sin hacer cambios
            }

            dialog.show()
        }
    }
}
