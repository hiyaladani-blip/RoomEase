package com.example.roomease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private var events: List<RoomEvent>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorTag: View = view.findViewById(R.id.eventColorTag)
        val title: TextView = view.findViewById(R.id.tvEventTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.title.text = event.name
        holder.colorTag.setBackgroundColor(event.color)
    }

    override fun getItemCount() = events.size

    fun updateList(newEvents: List<RoomEvent>) {
        events = newEvents
        notifyDataSetChanged()
    }
}