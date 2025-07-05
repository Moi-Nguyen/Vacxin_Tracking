package com.uth.vactrack.ui.UIUser

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uth.vactrack.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController = rememberNavController(),
    onBack: () -> Unit = { navController.popBackStack() },
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {}
) {
    val context = LocalContext.current
    val isNotificationEnabled = remember {
        mutableStateOf(NotificationManagerCompat.from(context).areNotificationsEnabled())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Optional menu */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Nguyen Van A", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("nguyenvana@gmail.com | 0315648987", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileOptionGroup(
                options = listOf(
                    Triple("Edit profile information", R.drawable.ic_edit) {
                        navController.navigate("edit_profile")
                    },
                    Triple("Notifications", R.drawable.ic_notification) {
                        // Mở cài đặt thông báo hệ thống
                        val intent = Intent().apply {
                            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                        context.startActivity(intent)
                    },
                    Triple("Language", R.drawable.ic_language) {}
                ),
                rightContent = listOf(
                    null,
                    {
                        Text(
                            if (isNotificationEnabled.value) "ON" else "OFF",
                            fontSize = 14.sp,
                            color = if (isNotificationEnabled.value) Color.Blue else Color.Gray
                        )
                    },
                    { Text("English", fontSize = 14.sp) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileOptionGroup(
                options = listOf(
                    Triple("Security", R.drawable.ic_security) {},
                    Triple("Theme", R.drawable.ic_theme) { onToggleTheme() }
                ),
                rightContent = listOf(
                    null,
                    {
                        Text(
                            if (isDarkTheme) "Dark mode" else "Light mode",
                            fontSize = 14.sp,
                            color = if (isDarkTheme) Color.LightGray else Color.Gray
                        )
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileOptionGroup(
                options = listOf(
                    Triple("Help & Support", R.drawable.ic_help) {},
                    Triple("Contact us", R.drawable.ic_contact) {},
                    Triple("Privacy policy", R.drawable.ic_privacy) {}
                )
            )
        }
    }
}

@Composable
fun ProfileOptionGroup(
    options: List<Triple<String, Int, () -> Unit>>,
    rightContent: List<@Composable (() -> Unit)?> = List(options.size) { null }
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            options.forEachIndexed { index, (label, iconRes, onClick) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onClick)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = label,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = label, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    rightContent.getOrNull(index)?.invoke()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(isDarkTheme = true, onToggleTheme = {})
}
