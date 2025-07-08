package com.uth.vactrack.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uth.vactrack.R
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

// Model tạm thời
data class Patient(val id: String, val name: String, val hasNewMessage: Boolean)

class FakeChatViewModel : ViewModel() {
    private val _patients = mutableStateListOf(
        Patient("1", "Nguyễn Văn A", true),
        Patient("2", "Trần Thị B", false)
    )
    val patients: List<Patient> get() = _patients
    fun fetchPatients() {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorOnlineConsultationScreen(
    navController: NavController,
    viewModel: FakeChatViewModel = viewModel(),
    onSelectPatient: (String) -> Unit = {}
) {
    val patients = listOf(
        Patient("1", "Nguyễn Trường Phục", true),
        Patient("2", "Nguyễn Văn B", true),
        Patient("3", "Hà Tuấn C", true),
        Patient("4", "Châu Thị Bích 3", true)
    )
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) { viewModel.fetchPatients() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
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

            Text("Online Consultation", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            IconButton(onClick = { /* Info action */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_info),
                    contentDescription = "Info",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TabButton(
                text = "Tất Cả",
                selected = selectedTab == 0,
                modifier = Modifier.weight(1f)
            ) { selectedTab = 0 }
            TabButton(
                text = "Chưa đọc",
                selected = selectedTab == 1,
                modifier = Modifier.weight(1f)
            ) { selectedTab = 1 }
        }
        val filteredPatients = if (selectedTab == 0) patients else patients.filter { it.hasNewMessage }
        LazyColumn(modifier = Modifier.padding(12.dp).weight(1f)) {
            items(filteredPatients) { patient ->
                PatientChatCardWithAvatar(name = patient.name, onClick = {
                    navController.navigate("chat_detail/${patient.id}/${patient.name}")
                })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        BottomNavBar(navController = navController, currentRoute = "chat_screen")
    }
}

@Composable
fun TabButton(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val selectedColor = Color(0xFF79747E)
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) selectedColor else Color(0xFFEFEFFF)
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (selected) BorderStroke(1.dp, selectedColor) else null,
        modifier = modifier
    ) {
        Text(
            text,
            color = if (selected) Color.White else Color.Black
        )
    }
}

@Composable
fun PatientChatCardWithAvatar(name: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EFFF)),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.avatar_user),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Tin Nhắn Mới", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        }
    }
}
