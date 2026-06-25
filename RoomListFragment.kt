package com.example.roomease

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class RoomListFragment : Fragment() {

    private val allRooms = mutableListOf(
        Room("1", "Krishna Hostel Room 120", "Main group for expenses", 4, "🏠"),
        Room("3", "Home Sweet Home", "Family group", 5, "🏡")
    )
    
    private val hiddenRoom = Room("2", "Summer Internship Flat", "Mumbai flatmates", 3, "🏢")

    private lateinit var adapter: RoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_room_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvRooms = view.findViewById<RecyclerView>(R.id.rvRooms)
        rvRooms.layoutManager = LinearLayoutManager(requireContext())
        adapter = RoomAdapter(allRooms) { room ->
            val bundle = Bundle().apply {
                putString("roomId", room.id)
                putString("roomName", room.name)
            }
            findNavController().navigate(R.id.action_roomList_to_expenses, bundle)
        }
        rvRooms.adapter = adapter

        view.findViewById<MaterialButton>(R.id.btnCreateRoom).setOnClickListener {
            showCreateRoomDialog()
        }

        view.findViewById<MaterialButton>(R.id.btnJoinRoom).setOnClickListener {
            showJoinRoomDialog()
        }
    }

    private fun showCreateRoomDialog() {
        val input = EditText(requireContext()).apply {
            hint = "Enter Room Name"
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Create New Room")
            .setView(input)
            .setPositiveButton("Create") { _, _ ->
                val name = input.text.toString()
                if (name.isNotEmpty()) {
                    val newRoom = Room((allRooms.size + 4).toString(), name, "New group", 1, "🏠")
                    allRooms.add(newRoom)
                    adapter.notifyItemInserted(allRooms.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showJoinRoomDialog() {
        val input = EditText(requireContext()).apply {
            hint = "Enter Invite Code"
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Join with Code")
            .setMessage("Try 'SUMMER50'")
            .setView(input)
            .setPositiveButton("Join") { _, _ ->
                val code = input.text.toString()
                if (code == "SUMMER50") {
                    if (!allRooms.contains(hiddenRoom)) {
                        allRooms.add(hiddenRoom)
                        adapter.notifyItemInserted(allRooms.size - 1)
                        Toast.makeText(requireContext(), "Joined Summer Internship Flat!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Already joined!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Invalid Code", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

class RoomAdapter(private val items: List<Room>, private val onClick: (Room) -> Unit) :
    RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvRoomName)
        val members: TextView = view.findViewById(R.id.tvRoomMembers)
        val icon: TextView = view.findViewById(R.id.tvRoomIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.members.text = holder.itemView.context.getString(R.string.members_count, item.memberCount)
        holder.icon.text = item.icon
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}