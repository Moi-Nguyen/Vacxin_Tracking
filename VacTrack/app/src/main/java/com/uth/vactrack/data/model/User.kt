package com.uth.vactrack.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val fullName: String? = null,
    val age: Int? = null,
    val dob: String? = null,
    val address: String? = null,
    val phone: String = "",
    val role: String? = null,
    val photoUrl: String = ""
) 