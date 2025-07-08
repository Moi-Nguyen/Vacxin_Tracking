package com.uth.vactrack.ui.doctor

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.uth.vactrack.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import com.google.gson.Gson
import java.net.URLEncoder

@Composable
fun DoctorHomeScreen(
    navController: NavController,
    token: String
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api = remember { retrofit.create(ApiService::class.java) }

    // Fake data để test UI nếu API chưa hoạt động
    val fakeAppointments = listOf(
            AppointmentBooking(
                _id = "1",
                userId = PatientInfo("Nguyễn Văn A", "a@example.com", "0123456789"),
                bookingDate = "2025-07-04T08:00:00Z",
                status = "confirmed",
                reason = "Fever"
            ),
    AppointmentBooking(
        _id = "2",
        userId = PatientInfo("Trần Thị B", "b@example.com", "0987654321"),
        bookingDate = "2025-07-04T09:00:00Z",
        status = "pending",
        reason = "Headache"
    ),
    AppointmentBooking(
        _id = "3",
        userId = PatientInfo("Lê Văn C", "c@example.com", "0911223344"),
        bookingDate = "2025-07-04T10:00:00Z",
        status = "confirmed",
        reason = "Cough"
    ),
    AppointmentBooking(
        _id = "4",
        userId = PatientInfo("Phạm Thị D", "d@example.com", "0933445566"),
        bookingDate = "2025-07-04T10:30:00Z",
        status = "pending",
        reason = "Nausea"
    )
    )


    var appointments by remember { mutableStateOf(fakeAppointments) }
    var isLoading by remember { mutableStateOf(false) }

    // Gọi API
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val result = withContext(Dispatchers.IO) {
                api.getShiftBookings("Bearer $token")
            }
            appointments = result.bookings
        } catch (e: Exception) {
            Log.e("DoctorHome", "API ERROR: ${e.message}")
            // Vẫn giữ fakeAppointments
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_vactrack_logo),
                contentDescription = "VacTrack Logo",
                modifier = Modifier.size(90.dp)
            )
            Image(
                painter = painterResource(R.drawable.avatar_doctor),
                contentDescription = "Doctor Avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoCard("Today's Appointments", appointments.size.toString())
            InfoCard("Patients Waiting", "2")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoCard("Today's Schedule", "10:00")
            InfoCard("Notifications", "1")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Next Visits", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.weight(1f)) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(appointments) { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            onStartConsultation = { selectedAppointment ->
                                val gson = Gson()
                                val appointmentJson = URLEncoder.encode(gson.toJson(selectedAppointment), "UTF-8")
                                navController.navigate("appointment_detail/$appointmentJson")
                            }
                        )
                    }
                }
            }
        }

        FeatureButton("Appointment List") {
            navController.navigate("appointment_list")
        }
        FeatureButton("Patients History") {
            navController.navigate("patients_history")
        }
        FeatureButton("Online Consultation")

        Spacer(modifier = Modifier.height(16.dp))

        BottomNavBar(navController = navController, currentRoute = currentRoute)
    }
}

// ==== COMPONENTS ====

@Composable
fun InfoCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD6E4FF)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 12.sp)
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FeatureButton(label: String, onClick: () -> Unit = {}) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF41CEEA)),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: AppointmentBooking,
    onStartConsultation: (AppointmentBooking) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.avatar_doctor),
                        contentDescription = "Patient",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(appointment.userId.name ?: "N/A")
                }
                Text(appointment.bookingDate.takeLast(5))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onStartConsultation(appointment) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C2A8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Start Consultation ➔", color = Color.White)
            }
        }
    }
}

// ==== API & DATA CLASS CHUNG (đặt dưới cùng file DoctorHomeScreen.kt) ====

interface ApiService {
    @GET("shifts/bookings")
    suspend fun getShiftBookings(@Header("Authorization") token: String): BookingResponse
}

data class BookingResponse(
    val shift: DoctorShift,
    val bookings: List<AppointmentBooking>
)

data class AppointmentBooking(
    val _id: String,
    val userId: PatientInfo,
    val bookingDate: String,
    val status: String,
    val reason: String?
)

data class PatientInfo(
    val name: String,
    val email: String?,
    val phone: String?
)

data class DoctorShift(
    val _id: String,
    val shiftDate: String,
    val shiftType: String,
    val startTime: String,
    val endTime: String
)

