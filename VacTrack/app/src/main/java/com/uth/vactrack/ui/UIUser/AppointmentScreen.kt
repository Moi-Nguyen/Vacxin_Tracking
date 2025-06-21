package com.uth.vactrack.ui.UIUser  // ✅ Đã sửa UIUser thành uiuser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.uth.vactrack.R
import com.uth.vactrack.ui.UIUser.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 12.dp, bottom = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_vactrack_logo),
                        contentDescription = "VacTrack Logo",
                        modifier = Modifier.height(40.dp)
                    )
                    Spacer(Modifier.weight(1f))
                }

                TopAppBar(
                    title = { Text("Appointments") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                contentDescription = "Info"
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(selectedIndex = 1)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            // --- Appointment For
            Text("Appointment For:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                    Text("Nguyen Van A", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                        contentDescription = "Dropdown"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Text("Book for an appointment")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_move),
                    contentDescription = "Next"
                )
            }

            // --- Other Section
            Spacer(modifier = Modifier.height(16.dp))
            Text("Other:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF00BCD4))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Name", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Birthday",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                            contentDescription = "Dropdown",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Phone", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Health Insurance ID", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(40.dp)
                    .width(120.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_move),
                    contentDescription = "Continue"
                )
            }

            // --- Appointment History
            Spacer(modifier = Modifier.height(24.dp))
            Text("Appointment History:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF0097A7), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("GM", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Gia Dinh Hospital", fontWeight = FontWeight.Bold)
                            Text("Vaccine Advice", fontSize = 12.sp, color = Color.Gray)
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = "Location",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("05/05/2025", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("1:00 PM", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp)) // space for bottom nav
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppointmentScreenPreview() {
    AppointmentScreen()
}
