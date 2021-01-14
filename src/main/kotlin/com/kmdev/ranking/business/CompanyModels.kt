package com.kmdev.ranking.business

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val site: String,
    val foundation: String
)

@Serializable
data class RankingResponse(
    val id: String,
    val name: String,
    val site: String,
    val score: Char,
    val likes: Int,
    val dislikes: Int
)

@Serializable
data class VoteRequest(
    val id: String,
    val type: String
)
