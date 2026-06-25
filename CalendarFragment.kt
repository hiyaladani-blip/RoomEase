package com.example.roomease

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class CalendarFragment : Fragment() {

    private val allEvents = mutableListOf(
        RoomEvent(15, 3, 2026, "Hiya's Birthday 🎂", Color.parseColor("#CE93D8")),
        RoomEvent(18, 3, 2026, "House Meeting 🤝", Color.parseColor("#66BB6A")),
        RoomEvent(22, 3, 2026, "Swanandi's Exam 📝", Color.parseColor("#4FC3F7")),
        RoomEvent(30, 3, 2026, "Rent Due 🏠", Color.parseColor("#EF5350"))
    )

    private var currentMonth = 3  // April (0-indexed)
    private var currentYear = 2026
    private var selectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    private lateinit var gridLayout: GridLayout
    private lateinit var tvMonth: TextView
    private lateinit var eventList: RecyclerView
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_calendar, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridLayout = view.findViewById(R.id.calendarGrid)
        tvMonth = view.findViewById(R.id.tvMonth)
        eventList = view.findViewById(R.id.rvEvents)

        adapter = EventAdapter(mutableListOf())
        eventList.layoutManager = LinearLayoutManager(requireContext())
        eventList.adapter = adapter

        view.findViewById<ImageButton>(R.id.btnPrevMonth).setOnClickListener {
            currentMonth--
            if (currentMonth < 0) { currentMonth = 11; currentYear-- }
            buildCalendar()
            updateEventList()
        }

        view.findViewById<ImageButton>(R.id.btnNextMonth).setOnClickListener {
            currentMonth++
            if (currentMonth > 11) { currentMonth = 0; currentYear++ }
            buildCalendar()
            updateEventList()
        }

        view.findViewById<FloatingActionButton>(R.id.btnAddEvent).setOnClickListener {
            showAddEventDialog()
        }

        buildCalendar()
        updateEventList()
    }

    private fun buildCalendar() {
        val monthNames = arrayOf("January","February","March","April","May","June",
            "July","August","September","October","November","December")
        tvMonth.text = "${monthNames[currentMonth]} $currentYear"

        gridLayout.removeAllViews()
        gridLayout.columnCount = 7

        val dayHeaders = listOf("Su","Mo","Tu","We","Th","Fr","Sa")
        dayHeaders.forEach { d ->
            val tv = TextView(requireContext()).apply {
                text = d
                textSize = 11f
                setTextColor(0xFF7A9AB8.toInt())
                gravity = android.view.Gravity.CENTER
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0; height = ViewGroup.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(2, 2, 2, 8)
                }
            }
            gridLayout.addView(tv)
        }

        val cal = Calendar.getInstance()
        cal.set(currentYear, currentMonth, 1)
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val today = Calendar.getInstance()
        val isCurrentMonth = today.get(Calendar.MONTH) == currentMonth && today.get(Calendar.YEAR) == currentYear
        val todayDay = today.get(Calendar.DAY_OF_MONTH)

        repeat(firstDayOfWeek) {
            val empty = Space(requireContext()).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0; height = 80
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
            }
            gridLayout.addView(empty)
        }

        for (day in 1..daysInMonth) {
            val hasEvent = allEvents.any { it.day == day && it.month == currentMonth && it.year == currentYear }
            val isToday = isCurrentMonth && day == todayDay
            val isSelected = day == selectedDay

            val tv = TextView(requireContext()).apply {
                text = day.toString()
                textSize = 13f
                gravity = android.view.Gravity.CENTER
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0; height = 80
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(3, 3, 3, 3)
                }
                when {
                    isSelected -> {
                        setBackgroundResource(R.drawable.circle_bg)
                        backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF38BDF8.toInt())
                        setTextColor(Color.WHITE)
                        setTypeface(null, Typeface.BOLD)
                    }
                    isToday -> {
                        setBackgroundResource(R.drawable.circle_bg)
                        backgroundTintList = android.content.res.ColorStateList.valueOf(0x330EA5E9.toInt()) // Faded blue for today if not selected
                        setTextColor(0xFF0EA5E9.toInt())
                        setTypeface(null, Typeface.BOLD)
                    }
                    hasEvent -> {
                        setTextColor(0xFF38BDF8.toInt())
                        setTypeface(null, Typeface.BOLD)
                    }
                    else -> {
                        setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.text_main))
                    }
                }
                setOnClickListener {
                    selectedDay = day
                    buildCalendar()
                    updateEventList()
                }
            }
            gridLayout.addView(tv)
        }
    }

    private fun updateEventList() {
        val filtered = allEvents.filter {
            it.day == selectedDay && it.month == currentMonth && it.year == currentYear
        }
        adapter.updateList(filtered)
    }

    private fun showAddEventDialog() {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 20)
        }

        val input = EditText(requireContext()).apply {
            hint = "What's happening? (e.g. Pizza night 🍕)"
            setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.text_main))
            setHintTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.text_hint))
        }
        layout.addView(input)

        val colorLabels = arrayOf("Bhavya (You) (Blue)", "Hiya (Purple)", "Swanandi (Green)", "Safa (Pink)", "Danger (Red)")
        val colorValues = intArrayOf(
            Color.parseColor("#4FC3F7"),
            Color.parseColor("#CE93D8"),
            Color.parseColor("#66BB6A"),
            Color.parseColor("#FFB74D"),
            Color.parseColor("#EF5350")
        )
        var selectedColor = colorValues[0]

        AlertDialog.Builder(requireContext())
            .setTitle("Add Event — Day $selectedDay")
            .setView(layout)
            .setSingleChoiceItems(colorLabels, 0) { _, which -> selectedColor = colorValues[which] }
            .setPositiveButton("Add") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    allEvents.add(RoomEvent(selectedDay, currentMonth, currentYear, name, selectedColor))
                    buildCalendar()
                    updateEventList()
                } else {
                    Toast.makeText(requireContext(), "Please enter an event name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
