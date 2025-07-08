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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URLDecoder
import com.uth.vactrack.R
import android.net.Uri

@Composable
fun PatientHistoryDetailScreen(navController: NavController, patientsJson: String?) {
    val patients: List<Triple<String, String, String>> = remember(patientsJson) {
        try {
            val type = object : TypeToken<List<Triple<String, String, String>>>() {}.type
            Gson().fromJson(URLDecoder.decode(patientsJson ?: "", "UTF-8"), type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Text("Patient History", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            IconButton(onClick = { }) {
                Icon(painter = painterResource(R.drawable.ic_info), contentDescription = "Info")
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Logo
        Image(
            painter = painterResource(R.drawable.ic_vactrack_logo),
            contentDescription = "VacTrack Logo",
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 8.dp)
        )

        // Danh sách bệnh nhân
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(patients) { (name, info, lastVisit) ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F9)),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
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
                            Column {
                                Text(name, fontWeight = FontWeight.Bold, color = Color.Black)
                                Text(info, fontSize = 13.sp, color = Color.DarkGray)
                                if (lastVisit != "—") {
                                    Text("Last visit: $lastVisit", fontSize = 13.sp, color = Color.DarkGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

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
    }
}
