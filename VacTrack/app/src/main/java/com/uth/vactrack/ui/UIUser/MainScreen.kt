package com.uth.vactrack.ui.UIUser

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
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
import com.uth.vactrack.ui.viewmodel.SharedViewModel
import com.uth.vactrack.ui.viewmodel.MainViewModel
import com.uth.vactrack.ui.viewmodel.HomeViewModel

@Composable
fun BottomNavigationBarMVVM(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    val tabs = listOf(
        Triple("Home", R.drawable.ic_home, 0),
        Triple("Record", R.drawable.ic_record, 1),
        Triple("Log", R.drawable.ic_log, 2),
        Triple("Profile", R.drawable.ic_user, 3)
    )
    Box {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .height(72.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .shadow(8.dp)
        ) {
            tabs.take(2).forEach { (label, icon, idx) ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(id = icon),
                            contentDescription = label,
                            tint = if (selectedIndex == idx) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = {
                        if (selectedIndex == idx) Text(label, fontSize = 12.sp)
                    },
                    selected = selectedIndex == idx,
                    onClick = { onTabSelected(idx) },
                    alwaysShowLabel = false
                )
            }
            Spacer(Modifier.weight(1f, true))
            tabs.drop(2).forEach { (label, icon, idx) ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(id = icon),
                            contentDescription = label,
                            tint = if (selectedIndex == idx) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = {
                        if (selectedIndex == idx) Text(label, fontSize = 12.sp)
                    },
                    selected = selectedIndex == idx,
                    onClick = { onTabSelected(idx) },
                    alwaysShowLabel = false
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(12.dp),
                modifier = Modifier
                    .offset(y = (-32).dp)
                    .size(68.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(36.dp))
            }
        }
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel(),
    homeViewModel: HomeViewModel = sharedViewModel.homeViewModel
) {
    val context = LocalContext.current
    val sharedState by sharedViewModel.sharedState.collectAsStateWithLifecycle()
    val mainState by mainViewModel.state.collectAsStateWithLifecycle()
    val state by homeViewModel.state.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(sharedState.userId, sharedState.token) {
        val userId = sharedState.userId
        val token = sharedState.token
        if (!userId.isNullOrBlank() && !token.isNullOrBlank()) {
            homeViewModel.loadAppointments(userId, token)
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBarMVVM(
                selectedIndex = selectedTab,
                onTabSelected = { idx ->
                    selectedTab = idx
                    when (idx) {
                        0 -> navController.navigate("home")
                        1 -> navController.navigate("appointment")
                        2 -> {}
                        3 -> navController.navigate("profile")
                    }
                },
                onFabClick = { navController.navigate("appointment") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 2.dp)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_logo_xoanen),
                    contentDescription = "Logo",
                    modifier = Modifier.height(80.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { selectedTab = 3; navController.navigate("profile") }
                )
            }

            val features = listOf(
                Triple(R.drawable.ic_book, "Book an Appointment", "Fast and easy booking"),
                Triple(R.drawable.ic_location, "Locate Vaccine Facility", "Bệnh viện Gia Định"),
                Triple(R.drawable.ic_emergency, "Request an Emergency", "Immediate support")
            )

            features.forEach { (icon, title, subtitle) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            when (title) {
                                "Book an Appointment" -> navController.navigate("appointment")
                                "Locate Vaccine Facility" -> {
                                    val ggmaps = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("geo:0,0?q=Bệnh+viện+Gia+Định,+Hồ+Chí+Minh")
                                    ).apply {
                                        setPackage("com.google.android.apps.maps")
                                    }
                                    context.startActivity(ggmaps)
                                }
                                "Request an Emergency" -> {
                                    val callIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:02838412692")
                                    }
                                    context.startActivity(callIntent)
                                }
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(MaterialTheme.colorScheme.background, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = title,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text(subtitle, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            }
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_move),
                            contentDescription = "Go",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Your Appointment", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(8.dp))

            val bookings = state.bookings
            if (bookings.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(bookings) { booking ->
                        Card(
                            modifier = Modifier.width(280.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = (booking.facilityName ?: "??").take(2).uppercase(),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(booking.facilityName ?: "Facility", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                                        Text(booking.serviceId, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                    }
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_location),
                                        contentDescription = "Map",
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(booking.date, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_clock),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(booking.time, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            } else {
                Text("No appointment history.", color = Color.Gray, modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Services", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
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
                            when (label) {
                                "Vaccination", "Screening", "Consultation" -> navController.navigate("select_service")
                                "Tracking" -> navController.navigate("tracking_booking")
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = label,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
