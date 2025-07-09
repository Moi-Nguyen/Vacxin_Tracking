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

@Composable
fun BottomNavigationBarMVVM(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    // Định nghĩa tab cho dễ quản lý
    val tabs = listOf(
        Triple("Home", R.drawable.ic_home, 0),
        Triple("Record", R.drawable.ic_record, 1),
        Triple("Log", R.drawable.ic_log, 2),
        Triple("Profile", R.drawable.ic_user, 3)
    )
    Box {
        // Thanh điều hướng bo góc, nổi
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
            Spacer(Modifier.weight(1f, true)) // Chừa chỗ cho FAB
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
        // FAB nổi bật ở giữa
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