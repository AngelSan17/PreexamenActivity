package com.example.preexamenactivity

import java.io.Serializable

data class Job(
    val id: Int,
    val name: String,
    val description: String,
    val city: String
) : Serializable
