package com.uth.vactrack.ui.UIUser

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.uth.vactrack.R
import com.uth.vactrack.ui.viewmodel.SelectTimeAndSlotViewModel
import com.uth.vactrack.ui.UIUser.BottomNavigationBar
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeAndSlotScreen(
    serviceName: String,
    bill: Int,
    navController: NavController,
    onBack: () -> Unit = {},
    sharedViewModel: com.uth.vactrack.ui.viewmodel.SharedViewModel,
    viewModel: SelectTimeAndSlotViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val user = sharedViewModel.sharedState.collectAsStateWithLifecycle().value.currentUser
    val userName = user?.name ?: "Guest"
    println("DEBUG USER IN SelectTimeAndSlotScreen: $user")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_logo_xoanen),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .height(40.dp)
                                .padding(end = 8.dp)
                        )
                        Text("Select Time and slot")
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
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Info card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
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
                        Column {
                            Text(userName, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Service: $serviceName", color = MaterialTheme.colorScheme.onSurface)
                            Text("Facility: Gia Dinh Hospital", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            // Progress bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .background(
                                    if (index <= 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        if (index < 3) Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            // Facility Info
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Gia Dinh Hospital", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("1D, No Trang Long, Phuong 7, Binh Thanh, Ho Chi Minh", color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            "Open • Weekday • 8 am - 5 pm",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Available slots
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Available Slots", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Select Date", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            // Slot rows
            state.availableSlots.forEach { (date, times) ->
                item {
                    Text(date, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(times) { time ->
                            Button(
                                onClick = {
                                    viewModel.selectDate(date)
                                    viewModel.selectTime(time)
                                    sharedViewModel.setSelectedFacilityId("64f8a1b2c3d4e5f6a7b8c9d2") // Gia Dinh Hospital
                                    val encodedService = URLEncoder.encode(serviceName, StandardCharsets.UTF_8.toString())
                                    val encodedDate = URLEncoder.encode(date, StandardCharsets.UTF_8.toString())
                                    val encodedTime = URLEncoder.encode(time, StandardCharsets.UTF_8.toString())
                                    navController.navigate("payment/$encodedService/$encodedDate/$encodedTime/$bill")
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(time, color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }
            }

            // Error message
            state.error?.let { error ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Text(
                            text = error,
                            color = Color(0xFFD32F2F),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // Loading indicator
            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
} 