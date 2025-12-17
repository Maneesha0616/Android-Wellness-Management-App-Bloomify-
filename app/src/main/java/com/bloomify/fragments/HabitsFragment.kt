package com.bloomify.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.bloomify.R
import com.bloomify.adapters.HabitAdapter
import com.bloomify.models.Habit
import com.bloomify.utils.PreferenceManager

class HabitsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var tvProgress: TextView
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var preferenceManager: PreferenceManager
    private val habits = mutableListOf<Habit>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_habits, container, false)

        preferenceManager = PreferenceManager(requireContext())

        recyclerView = view.findViewById(R.id.recyclerViewHabits)
        fabAdd = view.findViewById(R.id.fabAddHabit)
        tvProgress = view.findViewById(R.id.tvProgress)

        setupRecyclerView()
        loadHabits()

        fabAdd.setOnClickListener {
            showAddHabitDialog()
        }

        return view
    }

    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            habits,
            onItemClick = { habit -> showEditHabitDialog(habit) },
            onItemLongClick = { habit -> showDeleteDialog(habit) },
            onCheckChanged = { habit, isChecked ->
                habit.isCompleted = isChecked
                preferenceManager.saveHabits(habits)
                updateProgress()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = habitAdapter
    }

    private fun loadHabits() {
        habits.clear()
        habits.addAll(preferenceManager.getHabits())
        if (habits.isEmpty()) {
            habits.addAll(getDefaultHabits())
            preferenceManager.saveHabits(habits)
        }
        habitAdapter.notifyDataSetChanged()
        updateProgress()
    }

    private fun getDefaultHabits(): List<Habit> {
        return listOf(
            Habit(name = "Drink 8 glasses of water", icon = "💧"),
            Habit(name = "Meditate for 10 minutes", icon = "🧘"),
            Habit(name = "Exercise for 30 minutes", icon = "🏃"),
            Habit(name = "Read for 20 minutes", icon = "📚")
        )
    }

    private fun showAddHabitDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_habit, null)
        val etHabitName: EditText = dialogView.findViewById(R.id.etHabitName)
        val etHabitIcon: EditText = dialogView.findViewById(R.id.etHabitIcon)

    AlertDialog.Builder(requireContext())
            .setTitle("Add New Habit")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etHabitName.text.toString().trim()
                val icon = etHabitIcon.text.toString().trim().ifEmpty { "⭐" }
                if (name.isNotEmpty()) {
                    val newHabit = Habit(name = name, icon = icon)
                    habits.add(newHabit)
                    preferenceManager.saveHabits(habits)
                    habitAdapter.notifyItemInserted(habits.size - 1)
                    updateProgress()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditHabitDialog(habit: Habit) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_habit, null)
        val etHabitName: EditText = dialogView.findViewById(R.id.etHabitName)
        val etHabitIcon: EditText = dialogView.findViewById(R.id.etHabitIcon)

        etHabitName.setText(habit.name)
        etHabitIcon.setText(habit.icon)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Habit")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                habit.name = etHabitName.text.toString().trim()
                habit.icon = etHabitIcon.text.toString().trim().ifEmpty { "⭐" }
                preferenceManager.saveHabits(habits)
                habitAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDialog(habit: Habit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Habit")
            .setMessage("Are you sure you want to delete this habit?")
            .setPositiveButton("Delete") { _, _ ->
                val position = habits.indexOf(habit)
                habits.remove(habit)
                preferenceManager.saveHabits(habits)
                habitAdapter.notifyItemRemoved(position)
                updateProgress()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateProgress() {
        val completed = habits.count { it.isCompleted }
        val total = habits.size
        val percentage = if (total > 0) (completed * 100) / total else 0
        tvProgress.text = "Today's Progress: $completed/$total completed ($percentage%)"
    }
}