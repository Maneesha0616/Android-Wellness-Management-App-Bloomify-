package com.bloomify.models

data class User(
    val name: String,
    val email: String,
    val password: String,
    val joinedDate: Long = System.currentTimeMillis()
)