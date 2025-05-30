package com.uth.vactrack_app.data.models

data class ApiResponse<T>(
    val message: String,
    val data: T? = null
)