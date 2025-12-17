package com.bloomify.models

data class Habit(
    val id: String = System.currentTimeMillis().toString(),
    var name: String,
    var icon: String = "💧",
    var isCompleted: Boolean = false,
    val completedDate: String = ""
)