package com.kmdev.ranking.business

import java.time.LocalDate
import java.time.LocalDateTime

data class Company(
    val id: String,
    val name: String,
    val site: String,
    val foundation: LocalDate,
    val score: Char,
    val likes: Int = 0,
    val dislikes: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
