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
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface BookingApiService {
    @POST("/api/booking")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body request: BookingRequest
    ): BookingResponse
}

class AppointmentRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(AppConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val bookingApi = retrofit.create(BookingApiService::class.java)

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

    suspend fun bookAppointmentWithApi(token: String, request: BookingRequest): Result<BookingResponse> {
        return try {
            val response = bookingApi.createBooking("Bearer $token", request)
            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 