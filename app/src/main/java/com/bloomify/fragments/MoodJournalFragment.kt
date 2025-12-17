package com.bloomify.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.bloomify.R
import com.bloomify.adapters.MoodAdapter
import com.bloomify.models.MoodEntry
import com.bloomify.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class MoodJournalFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddMood: FloatingActionButton
    private lateinit var lineChart: LineChart
    private lateinit var btnToggleChart: MaterialButton
    private lateinit var btnShareSummary: MaterialButton
    private lateinit var moodAdapter: MoodAdapter
    private lateinit var preferenceManager: PreferenceManager
    private val moodEntries = mutableListOf<MoodEntry>()
    private var isChartVisible = false

    private val moodOptions = listOf(
        "😊" to "Happy" to 5,
        "😄" to "Excited" to 5,
        "😌" to "Calm" to 4,
        "😐" to "Neutral" to 3,
        "😔" to "Sad" to 2,
        "😰" to "Anxious" to 2,
        "😡" to "Angry" to 1
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mood_journal, container, false)

        preferenceManager = PreferenceManager(requireContext())

        recyclerView = view.findViewById(R.id.recyclerViewMoods)
        fabAddMood = view.findViewById(R.id.fabAddMood)
        lineChart = view.findViewById(R.id.lineChart)
        btnToggleChart = view.findViewById(R.id.btnToggleChart)
        btnShareSummary = view.findViewById(R.id.btnShareSummary)

        setupRecyclerView()
        setupChart()
        loadMoods()

        fabAddMood.setOnClickListener {
            showAddMoodDialog()
        }

        btnToggleChart.setOnClickListener {
            toggleChartVisibility()
        }

        btnShareSummary.setOnClickListener {
            shareMoodSummary()
        }

        return view
    }

    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter(
            moodEntries,
            onItemLongClick = { mood -> showDeleteDialog(mood) }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = moodAdapter
    }

    private fun setupChart() {
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            legend.textSize = 12f

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textSize = 10f
            }

            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
                axisMaximum = 6f
                textSize = 10f
            }

            axisRight.isEnabled = false
        }
    }

    private fun loadMoods() {
        moodEntries.clear()
        moodEntries.addAll(preferenceManager.getMoodEntries())
        moodEntries.sortByDescending { it.timestamp }
        moodAdapter.notifyDataSetChanged()
        updateChart()
    }

    private fun showAddMoodDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_mood, null)
        val moodContainer = dialogView.findViewById<ViewGroup>(R.id.moodContainer)
        val etNote = dialogView.findViewById<EditText>(R.id.etMoodNote)

        var selectedMood: Pair<Pair<String, String>, Int>? = null

        moodOptions.forEach { mood ->
            val moodView = LayoutInflater.from(requireContext()).inflate(R.layout.item_mood_selector, moodContainer, false)
            val tvEmoji = moodView.findViewById<TextView>(R.id.tvMoodEmoji)
            val tvName = moodView.findViewById<TextView>(R.id.tvMoodName)

            tvEmoji.text = mood.first.first
            tvName.text = mood.first.second

            moodView.setOnClickListener {
                selectedMood = mood
                for (i in 0 until moodContainer.childCount) {
                    moodContainer.getChildAt(i).isSelected = false
                }
                moodView.isSelected = true
            }

            moodContainer.addView(moodView)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("How are you feeling?")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                selectedMood?.let { mood ->
                    val note = etNote.text.toString().trim()
                    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                    val entry = MoodEntry(
                        emoji = mood.first.first,
                        moodName = mood.first.second,
                        note = note,
                        dateString = dateFormat.format(Date())
                    )
                    moodEntries.add(0, entry)
                    preferenceManager.saveMoodEntries(moodEntries)
                    moodAdapter.notifyItemInserted(0)
                    recyclerView.smoothScrollToPosition(0)
                    updateChart()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDialog(mood: MoodEntry) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Mood Entry")
            .setMessage("Are you sure you want to delete this mood entry?")
            .setPositiveButton("Delete") { _, _ ->
                val position = moodEntries.indexOf(mood)
                moodEntries.remove(mood)
                preferenceManager.saveMoodEntries(moodEntries)
                moodAdapter.notifyItemRemoved(position)
                updateChart()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun toggleChartVisibility() {
        isChartVisible = !isChartVisible
        lineChart.visibility = if (isChartVisible) View.VISIBLE else View.GONE
        btnToggleChart.text = if (isChartVisible) "Hide Chart" else "Show Mood Trend"
    }

    private fun updateChart() {
        if (moodEntries.isEmpty()) return

        val last7Days = moodEntries.filter {
            val daysDiff = (System.currentTimeMillis() - it.timestamp) / (1000 * 60 * 60 * 24)
            daysDiff <= 7
        }.sortedBy { it.timestamp }

        val entries = last7Days.mapIndexed { index, moodEntry ->
            val moodValue = moodOptions.find { it.first.first == moodEntry.emoji }?.second?.toFloat() ?: 3f
            Entry(index.toFloat(), moodValue)
        }

        if (entries.isNotEmpty()) {
            val dataSet = LineDataSet(entries, "Mood Trend (Last 7 Days)").apply {
                color = resources.getColor(R.color.primary, null)
                lineWidth = 2.5f
                setCircleColor(resources.getColor(R.color.primary, null))
                circleRadius = 5f
                setDrawCircleHole(false)
                valueTextSize = 10f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            lineChart.data = LineData(dataSet)

            lineChart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < last7Days.size) {
                        val date = Date(last7Days[index].timestamp)
                        SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
                    } else ""
                }
            }

            lineChart.invalidate()
        }
    }

    private fun shareMoodSummary() {
        if (moodEntries.isEmpty()) {
            Toast.makeText(requireContext(), "No mood entries to share", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate mood summary
        val summary = buildMoodSummary()

        // Create share intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "My Mood Summary - Bloomify")
            putExtra(Intent.EXTRA_TEXT, summary)
        }

        // Create chooser
        val chooser = Intent.createChooser(shareIntent, "Share your mood summary via")
        startActivity(chooser)
    }

    private fun buildMoodSummary(): String {
        val sb = StringBuilder()
        sb.appendLine("🌸 My Mood Summary - Bloomify 🌸")
        sb.appendLine("=" .repeat(35))
        sb.appendLine()

        // Overall statistics
        val totalMoods = moodEntries.size
        val moodCounts = moodEntries.groupBy { it.moodName }.mapValues { it.value.size }
        val mostCommonMood = moodCounts.maxByOrNull { it.value }

        sb.appendLine("📊 Overall Statistics:")
        sb.appendLine("Total mood entries: $totalMoods")
        sb.appendLine("Most frequent mood: ${mostCommonMood?.key} (${mostCommonMood?.value} times)")
        sb.appendLine()

        // Last 7 days summary
        val last7Days = moodEntries.filter {
            val daysDiff = (System.currentTimeMillis() - it.timestamp) / (1000 * 60 * 60 * 24)
            daysDiff <= 7
        }

        sb.appendLine("📅 Last 7 Days:")
        if (last7Days.isNotEmpty()) {
            val moodBreakdown = last7Days.groupBy { it.moodName }.mapValues { it.value.size }
            moodBreakdown.forEach { (mood, count) ->
                val emoji = moodOptions.find { it.first.second == mood }?.first?.first ?: "😊"
                sb.appendLine("  $emoji $mood: $count times")
            }
        } else {
            sb.appendLine("  No mood entries in the last 7 days")
        }
        sb.appendLine()

        // Recent moods (last 5)
        sb.appendLine("🕒 Recent Moods:")
        moodEntries.take(5).forEach { mood ->
            sb.appendLine("  ${mood.emoji} ${mood.moodName} - ${mood.dateString}")
            if (mood.note.isNotEmpty()) {
                sb.appendLine("     Note: ${mood.note}")
            }
        }
        sb.appendLine()

        sb.appendLine("=" .repeat(35))
        sb.appendLine("Generated by Bloomify - Your Personal Wellness Companion")

        return sb.toString()
    }
}