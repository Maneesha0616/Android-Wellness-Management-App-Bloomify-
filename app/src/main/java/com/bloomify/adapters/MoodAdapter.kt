package com.bloomify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bloomify.R
import com.bloomify.models.MoodEntry

class MoodAdapter(
    private val moods: List<MoodEntry>,
    private val onItemLongClick: (MoodEntry) -> Unit
) : RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    inner class MoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmoji: TextView = view.findViewById(R.id.tvMoodEmoji)
        val tvMoodName: TextView = view.findViewById(R.id.tvMoodName)
        val tvDate: TextView = view.findViewById(R.id.tvMoodDate)
        val tvNote: TextView = view.findViewById(R.id.tvMoodNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mood, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moods[position]
        holder.tvEmoji.text = mood.emoji
        holder.tvMoodName.text = mood.moodName
        holder.tvDate.text = mood.dateString
        holder.tvNote.text = mood.note
        holder.tvNote.visibility = if (mood.note.isEmpty()) View.GONE else View.VISIBLE

        holder.itemView.setOnLongClickListener {
            onItemLongClick(mood)
            true
        }
    }

    override fun getItemCount() = moods.size
}

