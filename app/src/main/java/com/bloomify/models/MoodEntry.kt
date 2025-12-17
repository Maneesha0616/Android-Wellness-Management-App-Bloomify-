package com.bloomify.models

data class MoodEntry(
    val id: String = System.currentTimeMillis().toString(),
    val emoji: String,
    val moodName: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String
)