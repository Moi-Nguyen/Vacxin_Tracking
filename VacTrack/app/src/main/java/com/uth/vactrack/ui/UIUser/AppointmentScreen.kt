package com.uth.vactrack.ui.UIUser

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.uth.vactrack.R
import com.uth.vactrack.ui.viewmodel.AppointmentViewModel
import com.uth.vactrack.ui.viewmodel.SharedViewModel
import com.uth.vactrack.ui.UIUser.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    navController: NavController,
    onBack: () -> Unit = { navController.popBackStack() },
    appointmentViewModel: AppointmentViewModel = viewModel(),
    sharedViewModel: SharedViewModel
) {
    val state by appointmentViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val user = sharedViewModel.sharedState.collectAsStateWithLifecycle().value.currentUser
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val appointmentHistory = listOf(1)
    val sharedState = sharedViewModel.sharedState.collectAsStateWithLifecycle().value
    val token = sharedState.token

    // Fetch booking history khi user đã đăng nhập và có token
    LaunchedEffect(user, token) {
        if (user != null && !token.isNullOrBlank()) {
            appointmentViewModel.fetchBookingHistory(token, user.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Appointment",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedIndex = 1)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { SectionTitle("Appointment For:") }
            item { AppointmentCard() }
            item {
                AppointmentButton(onClick = {
                    navController.navigate("select_service")
                })
            }

            item { SectionTitle("Other:") }
            item {
                OtherInfoCard(
                    name = state.name,
                    onNameChange = { appointmentViewModel.updateName(it) },
                    birthday = state.birthday,
                    onBirthdayChange = { appointmentViewModel.updateBirthday(it) },
                    phone = state.phone,
                    onPhoneChange = { appointmentViewModel.updatePhone(it) },
                    insuranceId = state.insuranceId,
                    onInsuranceIdChange = { appointmentViewModel.updateInsuranceId(it) }
                )
            }

            item {
                ContinueButton(
                    enabled = appointmentViewModel.isFormValid(),
                    onClick = {
                        if (user != null && !token.isNullOrBlank() &&
                            !sharedState.selectedServiceId.isNullOrBlank() &&
                            !sharedState.selectedFacilityId.isNullOrBlank()) {
                            appointmentViewModel.bookAppointment(
                                token = token,
                                userId = user.id,
                                serviceId = sharedState.selectedServiceId!!,
                                facilityId = sharedState.selectedFacilityId!!,
                                date = state.birthday, // TODO: lấy ngày từ UI chọn ngày
                                time = "09:00" // TODO: lấy giờ từ UI chọn giờ
                            )
                        } else {
                            dialogMessage = "Bạn cần đăng nhập và chọn dịch vụ/cơ sở để đặt lịch."
                            showDialog = true
                        }
                    }
                )
            }

            item { SectionTitle("Appointment History:") }
            if (state.history.isNotEmpty()) {
                items(state.history) { booking ->
                    AppointmentHistoryCard(booking)
                }
            } else {
                item {
                    Text("No appointment history.", color = Color.Gray, modifier = Modifier.padding(8.dp))
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
    if (state.message != null) {
        dialogMessage = state.message ?: ""
        showDialog = true
        appointmentViewModel.clearMessage()
    }
    if (state.error != null) {
        dialogMessage = state.error ?: ""
        showDialog = true
        appointmentViewModel.clearError()
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    if (state.message != null) {
                        navController.navigate("booking_success/service1/2024-07-01/09:00/500000")
                    }
                }) { Text("OK") }
            },
            title = { Text("Thông báo") },
            text = { Text(dialogMessage) }
        )
    }
}

@Composable
fun OtherInfoCard(
    name: String,
    onNameChange: (String) -> Unit,
    birthday: String,
    onBirthdayChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    insuranceId: String,
    onInsuranceIdChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = birthday,
                onValueChange = onBirthdayChange,
                label = { Text("Birthday (DD/MM/YYYY)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = insuranceId,
                onValueChange = onInsuranceIdChange,
                label = { Text("Insurance ID") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun AppointmentCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Vaccination Service",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Select your preferred vaccination service",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun AppointmentButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text("Select Service", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun ContinueButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray
        )
    ) {
        Text("Continue", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun AppointmentHistoryCard(booking: com.uth.vactrack.data.model.BookingDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Previous Appointment",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${booking.serviceId} - ${booking.facilityName ?: booking.facilityId}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                "Completed on ${booking.date} ${booking.time}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
} 