package com.example.roomease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoommateAdapter(
    private val roommates: List<Roommate>,
    private val onRemoveClick: (Roommate) -> Unit
) : RecyclerView.Adapter<RoommateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val initial: TextView = view.findViewById(R.id.tvInitial)
        val name: TextView = view.findViewById(R.id.tvName)
        val email: TextView = view.findViewById(R.id.tvEmail)
        val role: TextView = view.findViewById(R.id.tvRole)
        val btnRemove: View = view.findViewById(R.id.btnRemoveMember)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_roommate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = roommates[position]
        holder.initial.text = r.name.first().toString()
        // If the name contains "(You)", use only the first character of the real name
        if (r.name.contains(" (You)")) {
            holder.initial.text = r.name.substringBefore(" (You)").first().toString()
        }
        holder.name.text = r.name
        holder.email.text = r.email
        holder.role.text = r.role
        // Color the role badge
        if (r.role == "Admin") {
            holder.role.setTextColor(0xFF66BB6A.toInt())
        } else {
            holder.role.setTextColor(0xFF4FC3F7.toInt())
        }

        if (r.role == "Admin") {
            holder.btnRemove.visibility = View.GONE
        } else {
            holder.btnRemove.visibility = View.VISIBLE
            holder.btnRemove.setOnClickListener { onRemoveClick(r) }
        }
    }

    override fun getItemCount() = roommates.size
}