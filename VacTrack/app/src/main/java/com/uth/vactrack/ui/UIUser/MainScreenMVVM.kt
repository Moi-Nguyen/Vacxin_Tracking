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

@Composable
fun BottomNavigationBarMVVM(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    Box {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp,
            modifier = Modifier.height(64.dp)
        ) {
            NavigationBarItem(
                icon = { Icon(painterResource(id = R.drawable.ic_home), contentDescription = "Home") },
                label = { Text("Home") },
                selected = selectedIndex == 0,
                onClick = { onTabSelected(0) }
            )
            NavigationBarItem(
                icon = { Icon(painterResource(id = R.drawable.ic_record), contentDescription = "Record") },
                label = { Text("Record") },
                selected = selectedIndex == 1,
                onClick = { onTabSelected(1) }
            )
            Spacer(Modifier.weight(1f, true)) // Chừa chỗ cho FAB
            NavigationBarItem(
                icon = { Icon(painterResource(id = R.drawable.ic_log), contentDescription = "Log") },
                label = { Text("Log") },
                selected = selectedIndex == 2,
                onClick = { onTabSelected(2) }
            )
            NavigationBarItem(
                icon = { Icon(painterResource(id = R.drawable.ic_user), contentDescription = "Profile") },
                label = { Text("Profile") },
                selected = selectedIndex == 3,
                onClick = { onTabSelected(3) }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .offset(y = (-28).dp)
                    .size(56.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
fun MainScreenMVVM(
    navController: NavController,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val context = LocalContext.current
    val sharedState by sharedViewModel.sharedState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBarMVVM(
                selectedIndex = selectedTab,
                onTabSelected = { idx ->
                    selectedTab = idx
                    when (idx) {
                        0 -> navController.navigate("main")
                        1 -> navController.navigate("appointment")
                        2 -> {/* TODO: navController.navigate("log") */}
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

            // User info section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Welcome back, ${sharedState.currentUser?.name ?: "User"}!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Ready to schedule your next vaccination?",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
} 