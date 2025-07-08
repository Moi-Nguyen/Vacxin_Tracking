package com.uth.vactrack.data.repository

import com.uth.vactrack.data.model.Appointment
import com.uth.vactrack.data.model.AppointmentStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import com.uth.vactrack.config.AppConfig
import com.uth.vactrack.data.model.BookingRequest
import com.uth.vactrack.data.model.BookingResponse

class AppointmentRepository {
    
    suspend fun createAppointment(
        userId: String,
        serviceName: String,
        date: String,
        time: String,
        price: Int
    ): Result<Appointment> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/appointments")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                
                val body = JSONObject().apply {
                    put("userId", userId)
                    put("serviceName", serviceName)
                    put("date", date)
                    put("time", time)
                    put("price", price)
                }.toString()
                
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                
                if (conn.responseCode == 201) {
                    val appointment = Appointment(
                        id = json.getString("id"),
                        userId = json.getString("userId"),
                        serviceName = json.getString("serviceName"),
                        date = json.getString("date"),
                        time = json.getString("time"),
                        status = AppointmentStatus.valueOf(json.getString("status")),
                        price = json.getInt("price")
                    )
                    Result.success(appointment)
                } else {
                    Result.failure(Exception(json.optString("message", "Failed to create appointment")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getAppointmentsByUserId(userId: String): Result<List<Appointment>> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/appointments/user/$userId")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty("Content-Type", "application/json")
                
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                
                if (conn.responseCode == 200) {
                    val appointmentsArray = json.getJSONArray("appointments")
                    val appointments = mutableListOf<Appointment>()
                    
                    for (i in 0 until appointmentsArray.length()) {
                        val appointmentJson = appointmentsArray.getJSONObject(i)
                        val appointment = Appointment(
                            id = appointmentJson.getString("id"),
                            userId = appointmentJson.getString("userId"),
                            serviceName = appointmentJson.getString("serviceName"),
                            date = appointmentJson.getString("date"),
                            time = appointmentJson.getString("time"),
                            status = AppointmentStatus.valueOf(appointmentJson.getString("status")),
                            price = appointmentJson.getInt("price")
                        )
                        appointments.add(appointment)
                    }
                    Result.success(appointments)
                } else {
                    Result.failure(Exception(json.optString("message", "Failed to fetch appointments")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateAppointmentStatus(appointmentId: String, status: AppointmentStatus): Result<Appointment> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/appointments/$appointmentId/status")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "PUT"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                
                val body = JSONObject().apply {
                    put("status", status.name)
                }.toString()
                
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                
                if (conn.responseCode == 200) {
                    val appointment = Appointment(
                        id = json.getString("id"),
                        userId = json.getString("userId"),
                        serviceName = json.getString("serviceName"),
                        date = json.getString("date"),
                        time = json.getString("time"),
                        status = AppointmentStatus.valueOf(json.getString("status")),
                        price = json.getInt("price")
                    )
                    Result.success(appointment)
                } else {
                    Result.failure(Exception(json.optString("message", "Failed to update appointment")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun cancelAppointment(appointmentId: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/appointments/$appointmentId/cancel")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "PUT"
                conn.setRequestProperty("Content-Type", "application/json")
                
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                
                if (conn.responseCode == 200) {
                    Result.success(true)
                } else {
                    Result.failure(Exception(json.optString("message", "Failed to cancel appointment")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun bookAppointment(request: BookingRequest): Result<BookingResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/booking")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                val body = JSONObject().apply {
                    put("userId", request.userId)
                    put("serviceId", request.serviceId)
                    put("facilityId", request.facilityId)
                    put("date", request.date)
                    put("time", request.time)
                }.toString()
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                if (conn.responseCode == 200) {
                    val booking = json.optJSONObject("booking")
                    val bookingDetail = if (booking != null) {
                        com.uth.vactrack.data.model.BookingDetail(
                            id = booking.optString("id", ""),
                            userId = booking.optString("userId", ""),
                            serviceId = booking.optString("serviceId", ""),
                            facilityId = booking.optString("facilityId", ""),
                            date = booking.optString("date", ""),
                            time = booking.optString("time", ""),
                            status = booking.optString("status", "")
                        )
                    } else null
                    Result.success(
                        BookingResponse(
                            success = json.optBoolean("success", false),
                            message = json.optString("message", ""),
                            booking = bookingDetail
                        )
                    )
                } else {
                    Result.failure(Exception(json.optString("message", "Booking failed")))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
} 