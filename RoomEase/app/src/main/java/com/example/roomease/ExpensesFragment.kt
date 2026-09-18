package com.example.roomease

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExpensesFragment : Fragment() {

    private var currentRoomId: String? = null
    
    private val expensesMap = mapOf(
        "1" to mutableListOf(
            Expense(1, "Groceries", 1200.0, "Bhavya (You)", "🛒"),
            Expense(2, "Electricity Bill", 1500.0, "Hiya", "⚡"),
            Expense(3, "Internet", 800.0, "Swanandi", "🌐")
        ),
        "2" to mutableListOf(
            Expense(101, "Flat Deposit", 50000.0, "Bhavya (You)", "🏠"),
            Expense(102, "New Sofa", 15000.0, "Safa", "🛋️")
        ),
        "3" to mutableListOf(
            Expense(201, "Milk Delivery", 450.0, "Mom", "🥛"),
            Expense(202, "Newspaper", 200.0, "Dad", "📰")
        )
    )

    private var expensesList = mutableListOf<Expense>()
    private lateinit var adapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_expenses, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        currentRoomId = arguments?.getString("roomId") ?: "1"
        expensesList = (expensesMap[currentRoomId] ?: expensesMap["1"]!!).toMutableList()

        val roomName = arguments?.getString("roomName") ?: "Expenses"
        view.findViewById<TextView>(R.id.tvExpensesTitle).text = roomName

        val rvExpenses = view.findViewById<RecyclerView>(R.id.rvExpenses)
        val layoutInsights = view.findViewById<View>(R.id.layoutInsights)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAddExpense)
        val btnOverview = view.findViewById<TextView>(R.id.btnOverview)
        val btnInsights = view.findViewById<TextView>(R.id.btnInsights)

        adapter = ExpenseAdapter(expensesList)
        rvExpenses.layoutManager = LinearLayoutManager(requireContext())
        rvExpenses.adapter = adapter

        btnOverview.setOnClickListener {
            btnOverview.setBackgroundResource(R.drawable.tab_selected_bg)
            btnOverview.setTextColor(Color.WHITE)
            btnInsights.setBackgroundColor(Color.TRANSPARENT)
            btnInsights.setTextColor(Color.parseColor("#94A3B8"))
            rvExpenses.visibility = View.VISIBLE
            layoutInsights.visibility = View.GONE
            fabAdd.show()
        }

        btnInsights.setOnClickListener {
            btnInsights.setBackgroundResource(R.drawable.tab_selected_bg)
            btnInsights.setTextColor(Color.WHITE)
            btnOverview.setBackgroundColor(Color.TRANSPARENT)
            btnOverview.setTextColor(Color.parseColor("#94A3B8"))
            rvExpenses.visibility = View.GONE
            layoutInsights.visibility = View.VISIBLE
            fabAdd.hide()

            val pieChart = view.findViewById<PieChartView>(R.id.pieChartView)
            pieChart.setData(listOf(
                PieChartView.PieEntry(1200f, Color.parseColor("#38BDF8")),
                PieChartView.PieEntry(1500f, Color.parseColor("#818CF8")),
                PieChartView.PieEntry(800f, Color.parseColor("#34D399")),
                PieChartView.PieEntry(400f, Color.parseColor("#FBBF24")),
                PieChartView.PieEntry(600f, Color.parseColor("#F87171"))
            ))
        }

        fabAdd.setOnClickListener {
            showAddExpenseDialog(rvExpenses)
        }
    }

    private fun showAddExpenseDialog(recyclerView: RecyclerView) {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add New Expense")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val etDesc = EditText(context)
        etDesc.hint = "Description"
        layout.addView(etDesc)

        val etAmount = EditText(context)
        etAmount.hint = "Amount"
        etAmount.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        layout.addView(etAmount)

        val spinnerCategory = Spinner(context)
        val categories = arrayOf("🛒 Food", "⚡ Bills", "🏠 Rent", "🧹 Household", "🌐 Other")
        val adapterCat = ArrayAdapter(context, android.R.layout.simple_spinner_item, categories)
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapterCat
        layout.addView(spinnerCategory)

        builder.setView(layout)

        builder.setPositiveButton("Add") { _, _ ->
            val desc = etDesc.text.toString()
            val amountStr = etAmount.text.toString()
            val category = spinnerCategory.selectedItem.toString().split(" ")[0]

            if (desc.isNotEmpty() && amountStr.isNotEmpty()) {
                val newExpense = Expense(
                    expensesList.size + 1,
                    desc,
                    amountStr.toDouble(),
                    "Bhavya (You)",
                    category
                )
                expensesList.add(0, newExpense)
                adapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}

class ExpenseAdapter(private val expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDesc: android.widget.TextView = view.findViewById(R.id.tvDescription)
        val tvAmount: android.widget.TextView = view.findViewById(R.id.tvAmount)
        val tvPaidBy: android.widget.TextView = view.findViewById(R.id.tvPaidBy)
        val tvIcon: android.widget.TextView = view.findViewById(R.id.tvCategoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = expenses[position]
        holder.tvDesc.text = item.description
        holder.tvAmount.text = "₹${"%.2f".format(item.amount)}"
        holder.tvPaidBy.text = "Paid by ${item.paidBy}"
        holder.tvIcon.text = item.category
    }

    override fun getItemCount() = expenses.size
}