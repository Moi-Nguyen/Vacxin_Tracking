package com.uth.vactrack.ui.doctor

import androidx.lifecycle.viewmodel.compose.viewModel
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
import androidx.lifecycle.*
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.uth.vactrack.R
import com.uth.vactrack.model.Appointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import com.google.gson.Gson
import java.net.URLEncoder
import java.net.URLDecoder
@Composable
fun AppointmentListScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val viewModel: AppointmentListViewModel = viewModel(
        factory = AppointmentListViewModelFactory(backStackEntry.savedStateHandle)
    )


    val appointments = viewModel.appointments
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 90.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_vactrack_logo),
                contentDescription = "VacTrack Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                Text("Today's Appointments", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = "Info",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Dates :", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(8.dp))
                Image(painter = painterResource(R.drawable.calendar1), contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("13/05/2025")
                Spacer(modifier = Modifier.width(12.dp))
                Image(painter = painterResource(R.drawable.clock), contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("8:00 AM")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C2A8)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().height(45.dp)
            ) {
                Text("List Of Appointment", color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(painter = painterResource(R.drawable.ic_arrow_right), contentDescription = "Arrow", tint = Color.White, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(appointments) { appointment ->
                    AppointmentItem(data = appointment) {
                        val appointmentJson = URLEncoder.encode(Gson().toJson(appointment), "UTF-8")
                        navController.navigate("appointment_detail/$appointmentJson")
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}

@Composable
fun AppointmentItem(data: Appointment, onStartClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.avatar_doctor), contentDescription = null, modifier = Modifier.size(40.dp).clip(CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Text(data.name, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Text(data.time)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Age: ${data.age}    Insurance: ${if (data.hasInsurance) "Yes" else "No"}")

            Row {
                Image(painter = painterResource(R.drawable.calendar1), contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(data.date)
                Spacer(modifier = Modifier.width(12.dp))
                Image(painter = painterResource(R.drawable.clock), contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(data.time)
            }

            Row {
                Image(painter = painterResource(R.drawable.ic_chat), contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reason: ${data.reason}")
            }

            Text("🟡 Status: ${data.status}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("[Start Consultation]", color = Color(0xFF00C2A8), fontWeight = FontWeight.Medium, modifier = Modifier.clickable { onStartClick() })
                Text("[Cancel]", color = Color.Red, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { })
            }
        }
    }
}

// ✅ ViewModel (sử dụng lại các class từ DoctorHomeScreen)

class AppointmentListViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    var appointments = mutableStateListOf<Appointment>()
        private set

    init {
        val passed = savedStateHandle.get<List<Appointment>>("appointments")
        if (passed != null) {
            appointments.addAll(passed)
        } else {
            loadFromApiOrFake()
        }
    }

    private fun loadFromApiOrFake() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(AppointmentApiService::class.java)

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    api.getShiftBookings("Bearer your_token_here")
                }

                val converted = result.bookings.map {
                    Appointment(
                        name = it.userId.name,
                        age = 0,
                        hasInsurance = true,
                        date = it.bookingDate.take(10),
                        time = it.bookingDate.takeLast(5),
                        reason = it.reason ?: "",
                        status = it.status
                    )
                }

                appointments.clear()
                appointments.addAll(converted)
            } catch (e: Exception) {
                Log.e("AppointmentListVM", "API Error: ${e.message}")
                appointments.addAll(getFakeAppointments())
            }
        }
    }

    private fun getFakeAppointments() = listOf(
        Appointment("Nguyen Van A", 35, true, "13/05/2025", "08:00 AM", "Fever", "Waiting"),
        Appointment("Tran Thi B", 42, false, "13/05/2025", "09:30 AM", "Headache", "Confirmed"),
        Appointment("Le Van C", 29, true, "13/05/2025", "10:00 AM", "Cough", "Pending"),
    )
}

class AppointmentListViewModelFactory(
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentListViewModel(savedStateHandle) as T
    }
}

interface AppointmentApiService {
    @GET("shifts/bookings")
    suspend fun getShiftBookings(@Header("Authorization") token: String): BookingResponse
}

