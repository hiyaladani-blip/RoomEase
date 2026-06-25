package com.example.roomease

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SettingsFragment : Fragment() {

    private val roommates = mutableListOf(
        Roommate("Bhavya (You)", "bhavya@example.com", "Admin"),
        Roommate("Hiya", "hiya@example.com", "Member"),
        Roommate("Swanandi", "swanandi@example.com", "Member"),
        Roommate("Safa", "safa@example.com", "Member")
    )

    private lateinit var adapter: RoommateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvRoommates = view.findViewById<RecyclerView>(R.id.rvRoommates)
        rvRoommates.layoutManager = LinearLayoutManager(requireContext())
        adapter = RoommateAdapter(roommates, { roommate: Roommate ->
            removeRoommate(roommate)
        })
        rvRoommates.adapter = adapter

        view.findViewById<View>(R.id.btnShareInvite).setOnClickListener {
            Toast.makeText(context, "Invite Link Copied!", Toast.LENGTH_SHORT).show()
        }

        val btnToggleTheme = view.findViewById<View>(R.id.btnToggleTheme)
        val switchTheme = view.findViewById<SwitchCompat>(R.id.switchTheme)
        val tvThemeIcon = view.findViewById<TextView>(R.id.tvThemeIcon)
        val tvThemeStatus = view.findViewById<TextView>(R.id.tvThemeStatus)

        // Check if dark mode is active
        val isDarkMode = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
        
        switchTheme.isChecked = isDarkMode
        updateThemeUI(isDarkMode, tvThemeIcon, tvThemeStatus)

        btnToggleTheme.setOnClickListener {
            val nextDarkMode = !switchTheme.isChecked
            switchTheme.isChecked = nextDarkMode
            applyTheme(nextDarkMode)
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            applyTheme(isChecked)
        }

        view.findViewById<View>(R.id.tvLogout).setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun applyTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun updateThemeUI(isDarkMode: Boolean, icon: TextView, status: TextView) {
        if (isDarkMode) {
            icon.text = "🌙"
            status.text = "Dark mode"
        } else {
            icon.text = "☀️"
            status.text = "Light mode"
        }
    }

    private fun removeRoommate(roommate: Roommate) {
        if (roommate.role == "Admin") {
            Toast.makeText(requireContext(), "Cannot remove Admin", Toast.LENGTH_SHORT).show()
            return
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle("Remove Member")
            .setMessage("Are you sure you want to remove ${roommate.name}?")
            .setPositiveButton("Remove") { _, _ ->
                val index = roommates.indexOf(roommate)
                if (index != -1) {
                    roommates.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    Toast.makeText(requireContext(), "${roommate.name} removed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
