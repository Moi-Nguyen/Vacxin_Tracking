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
    val facilityName: String? = null,
    val date: String,
    val time: String,
    val status: String,
    val doseNumber: Int? = null,
    val price: Int? = null,
    val paymentStatus: String? = null,
    val doctorName: String? = null
) 