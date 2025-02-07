package com.example.agendaestudiantil

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class TaskAdapter(private var taskList: MutableList<Task>, private val taskController: TaskController) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(), Filterable {

    private var filteredTaskList: MutableList<Task> = taskList
    private var onTaskLongClickListener: ((Task) -> Unit)? = null

    /**
     * Actualiza la lista de tareas de forma eficiente.
     */
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

    /**
     * Filtra la lista de tareas por título.
     */
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

    /**
     * Establece un listener para detectar pulsaciones largas en las tareas.
     */
    fun setOnTaskLongClickListener(listener: (Task) -> Unit) {
        onTaskLongClickListener = listener
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleView: TextView = view.findViewById(R.id.taskTitle)
        private val descView: TextView = view.findViewById(R.id.taskDesc)
        private val dateView: TextView = view.findViewById(R.id.taskDate)
        private val locationIcon: ImageView = view.findViewById(R.id.locationIcon)

        init {
            // Evento de pulsación larga para mostrar opciones
            view.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = filteredTaskList[position]
                    onTaskLongClickListener?.invoke(task)
                }
                true
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
                    intent.putExtra("taskId", task.id)
                    intent.putExtra("location", task.location)
                    context.startActivity(intent)
                }
            }
        }
    }

    /**
     * Clase de utilidad para comparar listas de tareas en tiempo real.
     */
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
