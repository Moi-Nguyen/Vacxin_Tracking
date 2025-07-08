package com.uth.vactrack.data.model

data class Appointment(
    val id: String = "",
    val userId: String = "",
    val serviceName: String = "",
    val date: String = "",
    val time: String = "",
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val price: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AppointmentStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
} 