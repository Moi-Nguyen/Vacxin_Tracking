package com.uth.vactrack.data.model

data class BookingRequest(
    val userId: String,
    val serviceId: String,
    val facilityId: String,
    val date: String,
    val time: String,
    //val doseNumber: Int = 1,
    val notes: String? = null
) 