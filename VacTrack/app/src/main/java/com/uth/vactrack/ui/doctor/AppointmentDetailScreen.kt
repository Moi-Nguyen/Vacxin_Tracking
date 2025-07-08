package com.uth.vactrack.ui.doctor

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.gson.Gson
import com.uth.vactrack.R
import com.uth.vactrack.model.Appointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.net.URLDecoder
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.rememberCoroutineScope
import com.uth.vactrack.ui.doctor.AppointmentBooking

@Composable
fun AppointmentDetailScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val appointmentJson = backStackEntry.arguments?.getString("appointmentJson")
    val appointment = remember(appointmentJson) {
        try {
            Gson().fromJson(URLDecoder.decode(appointmentJson, "UTF-8"), AppointmentBooking::class.java)
        } catch (e: Exception) {
            Log.e("AppointmentDetail", "Decode error: ${e.message}")
            null
        }
    }

    if (appointment == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Không thể tải thông tin cuộc hẹn!", color = Color.Red)
        }
        return
    }

    var message by remember { mutableStateOf("") }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_vactrack_logo),
            contentDescription = "VacTrack Logo",
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .size(80.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Appointment Detail", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { }) {
                    Icon(painter = painterResource(R.drawable.ic_info), contentDescription = "Info", modifier = Modifier.size(28.dp))
                }
            }

            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Text(" Patient Info:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E5E5)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.avatar_doctor),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(40.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(appointment.userId?.name ?: "N/A", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Email: ${appointment.userId?.email ?: "N/A"}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Phone: ${appointment.userId?.phone ?: "N/A"}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Time: ${appointment.bookingDate ?: "N/A"}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Reason for Visit: ${appointment.reason ?: "N/A"}")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(R.drawable.ic_notes), contentDescription = "Notes Icon", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Diagnosis / Notes", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(painter = painterResource(R.drawable.ic_arrow_right), contentDescription = "Arrow", modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(" Actions", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ActionItem("✔ Mark as Completed") {
                        scope.launch {
                            markAsCompleted(appointment._id) { message = it }
                        }
                    }
                    ActionItem("✔ Suggest Lab Test")
                    Divider(Modifier.padding(vertical = 8.dp))
                    ActionItem(" Prescribe Medication")
                    ActionItem("❌ Cancel Appointment", isDestructive = true)
                }
            }

            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(" $message", color = Color.Gray)
            }
        }

        BottomNavBar(navController = navController, currentRoute = currentRoute)
    }
}

@Composable
fun ActionItem(text: String, isDestructive: Boolean = false, onClick: () -> Unit = {}) {
    Text(
        text = text,
        fontWeight = FontWeight.Medium,
        color = if (isDestructive) Color.Red else Color(0xFF00C2A8),
        modifier = Modifier.padding(vertical = 4.dp).clickable { onClick() }
    )
}

suspend fun markAsCompleted(appointmentId: String, onResult: (String) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(AppointmentDetailApi::class.java)

    try {
        val result = withContext(Dispatchers.IO) {
            api.markCompleted(appointmentId, "Bearer your_token_here")
        }
        onResult(result.message)
    } catch (e: Exception) {
        onResult("API failed: ${e.message}")
    }
}

interface AppointmentDetailApi {
    @POST("appointments/{id}/complete")
    suspend fun markCompleted(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): ApiResponse
}

data class ApiResponse(val message: String)
