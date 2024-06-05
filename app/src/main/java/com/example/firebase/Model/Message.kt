package com.example.firebase.Model

data class Message(
    val sender: String = "",
    val receiver: String = "",
    val content: String = "",
    val timestamp: Long = 0
)