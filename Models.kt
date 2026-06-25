package com.example.roomease

data class Expense(
    val id: Int,
    val description: String,
    val amount: Double,
    val paidBy: String,
    val category: String = "General"
)

data class Chore(
    val id: Int,
    val name: String,
    val assignedTo: String,
    var isCompleted: Boolean = false,
    val dueDate: String = "Today"
)
