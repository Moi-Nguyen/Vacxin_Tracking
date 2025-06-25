package com.uth.vactrack.ui.UIUser

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uth.vactrack.R

@Composable
fun BottomNavigationBar(
    selectedIndex: Int = 0,
    onRecordClick: () -> Unit = {}
) {
    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_record,
        R.drawable.ic_log,
        R.drawable.ic_user
    )
    val items = listOf("Home", "Record", "Log", "Profile")

    Box(modifier = Modifier.height(80.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(horizontal = 5.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icons.forEachIndexed { index, icon ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        if (items[index] == "Record") {
                            onRecordClick()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = items[index],
                        tint = if (index == selectedIndex) Color(0xFF007AFF) else Color.Gray,
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                if (index == selectedIndex) Color(0x332E66FF) else Color.Transparent,
                                shape = CircleShape
                            )
                            .padding(4.dp)
                    )
                    Text(
                        text = items[index],
                        fontSize = 18.sp,
                        color = if (index == selectedIndex) Color(0xFF007AFF) else Color.Gray
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onRecordClick,
            containerColor = Color(0xFF007AFF),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-28).dp)
                .size(56.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 0,
                onRecordClick = { navController.navigate("appointment") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 2.dp)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_vactrack_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.height(80.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }

            val features = listOf(
                Triple(R.drawable.ic_book, "Book an Appointment", "Fast and easy booking with minimal steps"),
                Triple(R.drawable.ic_location, "Locate Vaccine Facility", "Locate a nearby facility"),
                Triple(R.drawable.ic_emergency, "Request an Emergency", "Request an emergency service")
            )

            val featureColors = listOf(
                Color(0xFFD9EDFC),
                Color(0xFFE6F7F9),
                Color(0xFFE6F3F6)
            )

            features.forEachIndexed { index, (icon, title, subtitle) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            if (title == "Book an Appointment") {
                                navController.navigate("appointment")
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = featureColors[index % featureColors.size])
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = title,
                                    tint = Color(0xFF007AFF),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(subtitle, fontSize = 14.sp, color = Color.Gray)
                            }
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_move),
                            contentDescription = "Go",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Your Appointment", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF91C6FF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("GM", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("General Hospital", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Injection Vaccine (HPV, HIB)", fontSize = 14.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = "Map",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("12/05/2025", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(painter = painterResource(id = R.drawable.ic_clock), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("8:00 AM - 9:00 AM", fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Services", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))

            val services = listOf(
                Pair(R.drawable.ic_service_vaccination, "Vaccination"),
                Pair(R.drawable.ic_service_screening, "Screening"),
                Pair(R.drawable.ic_service_tracking, "Tracking"),
                Pair(R.drawable.ic_service_consultation, "Consultation")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                services.forEach { (icon, label) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            if (label == "Vaccination") {
                                navController.navigate("appointment")
                            }
                        }
                    ) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                            modifier = Modifier.size(64.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = label,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(label, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}
