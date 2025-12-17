package com.bloomify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bloomify.R
import com.bloomify.models.Habit

class HabitAdapter(
    private val habits: List<Habit>,
    private val onItemClick: (Habit) -> Unit,
    private val onItemLongClick: (Habit) -> Unit,
    private val onCheckChanged: (Habit, Boolean) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon: TextView = view.findViewById(R.id.tvHabitIcon)
        val tvName: TextView = view.findViewById(R.id.tvHabitName)
        val checkbox: CheckBox = view.findViewById(R.id.checkboxHabit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.tvIcon.text = habit.icon
        holder.tvName.text = habit.name
        holder.checkbox.isChecked = habit.isCompleted

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            onCheckChanged(habit, isChecked)
        }

        holder.itemView.setOnClickListener {
            onItemClick(habit)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(habit)
            true
        }
    }

    override fun getItemCount() = habits.size
}