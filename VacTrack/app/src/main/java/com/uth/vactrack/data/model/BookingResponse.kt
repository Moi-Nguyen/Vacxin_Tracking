package com.uth.vactrack.data.model

data class BookingResponse(
    val success: Boolean,
    val message: String,
    val booking: BookingDetail? = null
)

data class BookingDetail(
    val id: String,
    val userId: String,
    val serviceId: String,
    val facilityId: String,
    val date: String,
    val time: String,
    val status: String
) 