package com.uth.vactrack.ui.doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.uth.vactrack.R
import com.google.gson.Gson
import java.net.URLEncoder
import android.net.Uri

@Composable
fun PatientHistoryScreen(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val patients = listOf(
        Triple("Nguyen Van A", "42 | Male", "12 May 2025"),
        Triple("Nguyen Van B", "35 | Female", "3 May 2025"),
        Triple("Le Thi C", "28 | Female", "1 May 2025"),
        Triple("Pham Van D", "50 | Male", "25 Apr 2025"),
        Triple("Tran Thi E", "33 | Female", "20 Apr 2025"),
        Triple("Nguyen Van F", "45 | Male", "15 Apr 2025"),
        Triple("Vo Thi G", "38 | Female", "10 Apr 2025"),
        Triple("Bui Van H", "60 | Male", "5 Apr 2025"),
        Triple("Dang Thi I", "29 | Female", "28 Mar 2025"),
        Triple("Hoang Van J", "55 | Male", "22 Mar 2025")
    )


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 90.dp) // Chừa chỗ cho BottomNavBar
        ) {
            // Logo
            Image(
                painter = painterResource(R.drawable.ic_vactrack_logo),
                contentDescription = "VacTrack Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                Text("Patient History", fontSize = 18.sp, fontWeight = FontWeight.Bold)

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

            // Search (giả lập)
            OutlinedTextField(
                value = "",
                onValueChange = {},
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = "Search")
                },
                placeholder = { Text("Search patient by name...") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- PHẦN DANH SÁCH CUỘN ---
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(patients) { (name, info, lastVisit) ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9E9F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(R.drawable.avatar_user),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(name, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text(info, fontSize = 13.sp, color = Color.DarkGray)
                                    Text("Last visit: $lastVisit", fontSize = 13.sp, color = Color.DarkGray)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        navController.navigate(
                                            "patient_info/" +
                                                    Uri.encode(name) + "/" +
                                                    Uri.encode(info) + "/" +
                                                    Uri.encode(lastVisit)
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFADCE5)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("View Record", color = Color.Black, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // --- NÚT "MORE" CỐ ĐỊNH ---
            Spacer(modifier = Modifier.height(12.dp))
            val patientsJson = URLEncoder.encode(Gson().toJson(patients), "UTF-8")
            Button(
                onClick = { navController.navigate("patients_history_detail/$patientsJson") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B9C6)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("More", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
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
