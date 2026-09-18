package com.example.roomease

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChoresFragment : Fragment() {

    private val choresList = mutableListOf(
        Chore(1, "Clean Kitchen", "Bhavya (You)", false, "Today"),
        Chore(2, "Vacuum Living Room", "Hiya", false, "Tomorrow"),
        Chore(3, "Take Out Trash", "Swanandi", true, "Yesterday"),
        Chore(4, "Clean Bathroom", "Safa", false, "Friday")
    )

    private lateinit var pendingAdapter: ChoreAdapter
    private lateinit var completedAdapter: ChoreAdapter
    private lateinit var tvYourTasksCount: TextView
    private lateinit var tvCompletedCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_chores, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvPending = view.findViewById<RecyclerView>(R.id.rvPendingChores)
        val rvCompleted = view.findViewById<RecyclerView>(R.id.rvCompletedChores)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAddChore)
        tvYourTasksCount = view.findViewById(R.id.tvYourTasksCount)
        tvCompletedCount = view.findViewById(R.id.tvCompletedTasksCount)

        setupRecyclerViews(rvPending, rvCompleted)
        updateStats()

        fabAdd.setOnClickListener {
            showAddChoreDialog()
        }
    }

    private fun setupRecyclerViews(rvPending: RecyclerView, rvCompleted: RecyclerView) {
        pendingAdapter = ChoreAdapter(choresList.filter { !it.isCompleted }.toMutableList()) { chore ->
            chore.isCompleted = true
            refreshLists()
        }
        completedAdapter = ChoreAdapter(choresList.filter { it.isCompleted }.toMutableList()) { chore ->
            chore.isCompleted = false
            refreshLists()
        }

        rvPending.layoutManager = LinearLayoutManager(requireContext())
        rvPending.adapter = pendingAdapter

        rvCompleted.layoutManager = LinearLayoutManager(requireContext())
        rvCompleted.adapter = completedAdapter
    }

    private fun refreshLists() {
        pendingAdapter.updateData(choresList.filter { !it.isCompleted })
        completedAdapter.updateData(choresList.filter { it.isCompleted })
        updateStats()
    }

    private fun updateStats() {
        val yourTasks = choresList.count { it.assignedTo == "Bhavya (You)" && !it.isCompleted }
        val completed = choresList.count { it.isCompleted }
        tvYourTasksCount.text = yourTasks.toString()
        tvCompletedCount.text = completed.toString()
    }

    private fun showAddChoreDialog() {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Assign New Chore")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val etName = EditText(context)
        etName.hint = "Chore Name"
        layout.addView(etName)

        val spinnerUser = Spinner(context)
        val users = arrayOf("Bhavya (You)", "Hiya", "Swanandi", "Safa")
        val adapterUser = ArrayAdapter(context, android.R.layout.simple_spinner_item, users)
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUser.adapter = adapterUser
        layout.addView(spinnerUser)

        builder.setView(layout)

        builder.setPositiveButton("Add") { _, _ ->
            val name = etName.text.toString()
            val user = spinnerUser.selectedItem.toString()

            if (name.isNotEmpty()) {
                val newChore = Chore(choresList.size + 1, name, user, false, "Today")
                choresList.add(0, newChore)
                refreshLists()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}

class ChoreAdapter(
    private var chores: MutableList<Chore>,
    private val onStatusChanged: (Chore) -> Unit
) : RecyclerView.Adapter<ChoreAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbChore: CheckBox = view.findViewById(R.id.cbChore)
        val tvName: TextView = view.findViewById(R.id.tvChoreName)
        val tvDue: TextView = view.findViewById(R.id.tvDueDate)
        val tvAssigned: TextView = view.findViewById(R.id.tvAssignedTo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chore, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chores[position]
        holder.tvName.text = item.name
        holder.tvDue.text = "Due ${item.dueDate}"
        holder.tvAssigned.text = item.assignedTo
        holder.cbChore.setOnCheckedChangeListener(null)
        holder.cbChore.isChecked = item.isCompleted

        holder.cbChore.setOnCheckedChangeListener { _, _ ->
            onStatusChanged(item)
        }
    }

    override fun getItemCount() = chores.size

    fun updateData(newItems: List<Chore>) {
        chores.clear()
        chores.addAll(newItems)
        notifyDataSetChanged()
    }
}
