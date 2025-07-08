package com.uth.vactrack.ui.doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.uth.vactrack.R

@Composable
fun SettingsScreen(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 90.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_vactrack_logo),
                contentDescription = "VacTrack Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = "Info",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.avatar_user),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Bác Sĩ", fontWeight = FontWeight.Medium, color = Color.Black)
                    Text("Nguyen Pham Thien Phuoc", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            SettingsItem("Lịch Sử Khám Bệnh")
            Divider()
            SettingsItem("Hồ Sơ")
            Divider()
            SettingsItem("Đánh Giá Từ Khách Hàng")
            Divider()
            SettingsItem("Hỗ Trợ")
            Divider()
            SettingsItem("Cài Đặt Mật Khẩu")

            Spacer(modifier = Modifier.height(32.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Đăng Xuất",
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.clickable { }.padding(start = 4.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}

@Composable
fun SettingsItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.Medium, color = Color.Black)
        Icon(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "Arrow",
            modifier = Modifier.size(20.dp)
        )
    }
}
