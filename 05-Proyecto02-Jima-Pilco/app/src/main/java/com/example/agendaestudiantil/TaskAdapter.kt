package com.example.agendaestudiantil

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.widget.PopupMenu
import java.util.*

class TaskAdapter(private var taskList: MutableList<Task>, private val dbHelper: DBHelper) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(), Filterable {

    private var filteredTaskList: MutableList<Task> = taskList

    fun updateList(newList: MutableList<Task>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(filteredTaskList, newList))
        filteredTaskList.clear()
        filteredTaskList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
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
                val query = constraint?.toString()?.lowercase(Locale.getDefault()).orEmpty()
                val filteredList = if (query.isEmpty()) {
                    taskList
                } else {
                    taskList.filter { it.title.lowercase(Locale.getDefault()).contains(query) }
                        .toMutableList()
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                updateList(results?.values as MutableList<Task>)
            }
        }
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleView: TextView = view.findViewById(R.id.taskTitle)
        private val descView: TextView = view.findViewById(R.id.taskDesc)
        private val dateView: TextView = view.findViewById(R.id.taskDate)
        private val locationIcon: ImageView = view.findViewById(R.id.locationIcon) // ← Añadir esta línea

        init {
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

            // Mostrar el icono si la tarea tiene ubicación
            if (task.location.isNullOrEmpty()) {
                locationIcon.visibility = View.GONE
            } else {
                locationIcon.visibility = View.VISIBLE
                locationIcon.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, MapActivity::class.java)
                    intent.putExtra("location", task.location)
                    context.startActivity(intent)
                }
            }
        }


        private fun showOptionsMenu(task: Task) {
            val context = itemView.context
            val popupMenu = PopupMenu(context, itemView)
            popupMenu.menuInflater.inflate(R.menu.task_options_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        showEditDialog(task)
                        true
                    }
                    R.id.menu_delete -> {
                        showDeleteConfirmation(task)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
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

                if (dbHelper.updateTask(task)) {
                    updateList(dbHelper.getAllTasks().toMutableList())
                    Toast.makeText(itemView.context, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(itemView.context, "Error al actualizar tarea", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }

            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        private fun showDeleteConfirmation(task: Task) {
            val context = itemView.context
            AlertDialog.Builder(context)
                .setTitle("Eliminar Tarea")
                .setMessage("¿Seguro que deseas eliminar esta tarea?")
                .setPositiveButton("Sí") { _, _ ->
                    if (dbHelper.deleteTask(task.id ?: 0)) {
                        updateList(dbHelper.getAllTasks().toMutableList())
                        Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    class TaskDiffCallback(
        private val oldList: List<Task>,
        private val newList: List<Task>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
