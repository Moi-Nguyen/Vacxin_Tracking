package com.uth.vactrack_app.data.models

data class LoginResponse(
    val message: String,
    val token: String,
    val user: UserData
)
