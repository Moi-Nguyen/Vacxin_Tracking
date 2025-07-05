package com.uth.vactrack.ui.UIUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.uth.vactrack.ui.theme.VacTrackTheme

data class Booking(
    val name: String,
    val service: String,
    val facility: String,
    val date: String,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingBookingScreen(navController: NavController = rememberNavController()) {

    val currentBookings = listOf(
        Booking("Nguyen Van A", "Vaccine Services", "Gia Dinh Hospital", "13/05/2025", "8:00 AM"),
        Booking("Nguyen Van A", "HPV Vaccine", "City Hospital", "15/05/2025", "9:00 AM")
    )

    val historyBookings = listOf(
        Booking("Nguyen Van A", "Vaccine Services", "Gia Dinh Hospital", "10/04/2025", "10:00 AM"),
        Booking("Nguyen Van A", "COVID-19 Vaccine", "City Hospital", "01/03/2025", "2:00 PM")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tracking",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            BottomNavigationBar(selectedIndex = 2)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("appointment") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Appointment")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Current calendar:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(currentBookings) { booking ->
                    BookingCardExpanded(booking)
                }
            }

            Text("History calendar:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(historyBookings) { booking ->
                    BookingCardExpanded(booking)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun BookingCardExpanded(booking: Booking) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .width(320.dp)
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(booking.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Services: ${booking.service}", fontSize = 16.sp)
            Text("Facility: ${booking.facility}", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Date",
                    modifier = Modifier.size(18.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("${booking.date}   ${booking.time}", fontSize = 15.sp)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTrackingBookingScreen() {
    VacTrackTheme {
        TrackingBookingScreen()
    }
}
