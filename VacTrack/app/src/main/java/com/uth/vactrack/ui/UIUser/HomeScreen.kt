package com.uth.vactrack.ui.UIUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uth.vactrack.R
import com.uth.vactrack.ui.viewmodel.HomeViewModel
import com.uth.vactrack.ui.viewmodel.SharedViewModel

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    homeViewModel: HomeViewModel = viewModel(),
    sharedViewModel: SharedViewModel
) {
    val state by homeViewModel.state.collectAsStateWithLifecycle()
    val sharedState by sharedViewModel.sharedState.collectAsStateWithLifecycle()
    var menuExpanded by remember { mutableStateOf(false) }

    // Load appointments khi component được tạo
    LaunchedEffect(Unit) {
        val userId = sharedState.userId
        val token = sharedState.token
        if (!userId.isNullOrBlank() && !token.isNullOrBlank()) {
            homeViewModel.loadAppointments(userId, token)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // ===== Top logo + avatar =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 10.dp, end = 10.dp, bottom = 12.dp)
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_logo_xoanen),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_avatar),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("profile")
                    }
            )
        }

        // ===== VACTRACK title bar =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0xFF2D25C9))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "VACTRACK",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier
                        .size(45.dp)
                        .padding(end = 10.dp)
                )
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { menuExpanded = true }
                    )

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier
                            .background(Color(0xFF2D25C9), shape = RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Feedback", color = Color.White, fontSize = 14.sp)
                            },
                            onClick = {
                                menuExpanded = false
                                navController.navigate("feedback")
                            },
                            colors = MenuDefaults.itemColors(textColor = Color.White)
                        )
                        DropdownMenuItem(
                            text = {
                                Text("Logout", color = Color.White, fontSize = 14.sp)
                            },
                            onClick = {
                                menuExpanded = false
                                sharedViewModel.logout()
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            colors = MenuDefaults.itemColors(textColor = Color.White)
                        )
                    }
                }
            }
        }

        // ===== Appointments Section =====
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Lịch hẹn của bạn",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D25C9),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF2D25C9))
                    }
                } else {
                    val allBookings = state.bookings + state.appointments.map { appointment ->
                        com.uth.vactrack.data.model.BookingDetail(
                            id = appointment.id,
                            userId = appointment.userId,
                            serviceId = "",
                            facilityId = "",
                            facilityName = null,
                            date = appointment.date,
                            time = appointment.time,
                            status = appointment.status.name,
                            doseNumber = 1,
                            price = appointment.price,
                            paymentStatus = null,
                            doctorName = null
                        )
                    }
                    
                    if (allBookings.isEmpty()) {
                        // Hiển thị thông báo khi không có lịch hẹn
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                contentDescription = "No appointments",
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Bạn chưa có lịch hẹn nào",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Hãy đặt lịch hẹn để được tư vấn và tiêm chủng",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.navigate("main") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D25C9)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Đặt lịch hẹn ngay", color = Color.White)
                            }
                        }
                    } else {
                        // Hiển thị danh sách lịch hẹn
                        allBookings.take(3).forEach { booking ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        contentDescription = "Appointment",
                                        tint = Color(0xFF2D25C9),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = booking.facilityName ?: "Dịch vụ",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "${booking.date} - ${booking.time}",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        if (!booking.status.isNullOrBlank()) {
                                            Text(
                                                text = "Trạng thái: ${booking.status}",
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        
                        if (allBookings.size > 3) {
                            TextButton(
                                onClick = { navController.navigate("appointment") },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(
                                    text = "Xem tất cả (${allBookings.size})",
                                    color = Color(0xFF2D25C9),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // ===== Doctor banner =====
        Image(
            painter = painterResource(id = R.drawable.ic_img_doctor),
            contentDescription = "Doctor Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
        )

        // ===== Seminar banner =====
        Image(
            painter = painterResource(id = R.drawable.ic_img_seminar),
            contentDescription = "Seminar Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        // ===== Home Button (replaces "Learn More") =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("main") },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D25C9)),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
            ) {
                Text(text = "Home", fontSize = 16.sp, color = Color.White)
            }
        }

        // ===== Info Section =====
        Image(
            painter = painterResource(id = R.drawable.ic_img_info_footer),
            contentDescription = "Info Footer",
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(vertical = 16.dp, horizontal = 16.dp)
        )
    }
} 