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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uth.vactrack.R
import com.uth.vactrack.ui.theme.VacTrackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    navController: NavController = rememberNavController(),
    onBack: () -> Unit = { navController.popBackStack() }
) {
    val name = remember { mutableStateOf("") }
    val birthday = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val insuranceId = remember { mutableStateOf("") }

    val isBirthdayValid = birthday.value.matches(
        Regex("""^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\d{4})$""")
    )
    val isPhoneValid = phone.value.length in 10..12 && phone.value.all { it.isDigit() }
    val isFormValid = name.value.isNotBlank() &&
            birthday.value.isNotBlank() && isBirthdayValid &&
            phone.value.isNotBlank() && isPhoneValid &&
            insuranceId.value.isNotBlank()

    val appointmentHistory = listOf(1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_logo_xoanen),
                            contentDescription = "VacTrack Logo",
                            modifier = Modifier
                                .height(32.dp)
                                .padding(end = 8.dp)
                        )
                        Text("Appointments")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info),
                            contentDescription = "Info"
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
                    name = name.value,
                    onNameChange = { name.value = it },
                    birthday = birthday.value,
                    onBirthdayChange = { birthday.value = it },
                    phone = phone.value,
                    onPhoneChange = { phone.value = it },
                    insuranceId = insuranceId.value,
                    onInsuranceIdChange = { insuranceId.value = it }
                )
            }

            item {
                ContinueButton(
                    enabled = isFormValid,
                    onClick = {
                        navController.navigate("select_service")
                    }
                )
            }

            item { SectionTitle("Appointment History:") }
            items(appointmentHistory) {
                AppointmentHistoryCard()
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
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
    val birthdayValid = birthday.matches(Regex("""^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\d{4})$"""))
    val phoneValid = phone.length in 10..12 && phone.all { it.isDigit() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = birthday,
                onValueChange = onBirthdayChange,
                label = { Text("Birthday (dd/MM/yyyy)") },
                isError = birthday.isNotBlank() && !birthdayValid,
                supportingText = {
                    if (birthday.isNotBlank() && !birthdayValid) {
                        Text(
                            "Invalid format. Use dd/MM/yyyy",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = { Text("Phone") },
                isError = phone.isNotBlank() && !phoneValid,
                supportingText = {
                    if (phone.isNotBlank() && !phoneValid) {
                        Text(
                            "Phone must be 10–12 digits",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = insuranceId,
                onValueChange = onInsuranceIdChange,
                label = { Text("Health Insurance ID") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
fun AppointmentCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_avatar),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Nguyen Van A", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                contentDescription = "Dropdown",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AppointmentButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text("Book for an appointment")
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_move),
            contentDescription = "Next"
        )
    }
}

@Composable
fun ContinueButton(enabled: Boolean = true, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .align(Alignment.Center)
                .height(40.dp)
                .width(120.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_move),
                contentDescription = "Continue"
            )
        }
    }
}

@Composable
fun AppointmentHistoryCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("GM", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Gia Dinh Hospital", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("Vaccine Advice", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Calendar",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("05/05/2025", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = "Clock",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("1:00 PM", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppointmentScreenPreview() {
    VacTrackTheme {
        AppointmentScreen()
    }
}
