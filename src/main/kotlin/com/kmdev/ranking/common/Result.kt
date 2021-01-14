package com.kmdev.ranking.common

sealed class Result<out T> {}

data class Success<T>(val value: T) : Result<T>()
data class Fail<T>(val cause: String, val error: Exception? = null): Result<T>()
