package com.uth.vactrack.ui.doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uth.vactrack.R
import androidx.compose.ui.draw.shadow

@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Triple(R.drawable.ic_calendar, "Calendar", "appointment_list"),
        Triple(R.drawable.ic_person, "Person", "patients_history"),
        Triple(R.drawable.ic_chat, "Chat", "chat_screen"),
        Triple(R.drawable.ic_settings, "Settings", "settings_screen")
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (icon, label, route) ->
                val isSelected = when (route) {
                    "appointment_list" -> currentRoute == "appointment_list" || currentRoute.startsWith("appointment_detail")
                    "patients_history" -> currentRoute == "patients_history"
                    else -> currentRoute == route
                }

                val tintColor = if (isSelected) Color(0xFF00C2A8) else Color.Gray

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            if (!isSelected) {
                                navController.navigate(route) {
                                    popUpTo("doctor_home") { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                        .padding(horizontal = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = label,
                        modifier = Modifier.size(24.dp),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(tintColor)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = label, fontSize = 12.sp, color = tintColor)
                }
            }
        }
    }
}

