package com.uth.vactrack.ui.doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uth.vactrack.R
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel

data class Message(val id: String, val sender: String, val content: String, val timestamp: String)
class FakeChatDetailViewModel : ViewModel() {
    private val _messages = mutableStateListOf(
        Message("1", "doctor", "Xin chào, tôi là bác sĩ.", ""),
        Message("2", "patient", "Chào bác sĩ!", "")
    )
    val messages: List<Message> get() = _messages
    fun fetchMessages(patientId: String) { /* fake */ }
    fun sendMessage(patientId: String, message: Message) { _messages.add(message) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorChatDetailScreen(
    navController: NavController,
    patientName: String,
    patientId: String,
    viewModel: FakeChatDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val messages = viewModel.messages
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(patientId) {
        viewModel.fetchMessages(patientId)
    }

    LaunchedEffect(messages.size) {
        coroutineScope.launch {
            listState.animateScrollToItem(messages.size)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
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
            Text(
                patientName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Info */ }) {
                Icon(Icons.Default.Info, contentDescription = "Info", modifier = Modifier.size(28.dp))
            }
        }
        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
        // No message bubbles if empty
        Spacer(modifier = Modifier.weight(1f))
        // Input bar at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEEEEEE))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(painter = painterResource(R.drawable.ic_add), contentDescription = null)
            }
            IconButton(onClick = { }) {
                Icon(painter = painterResource(R.drawable.ic_voicechat), contentDescription = null)
            }
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Nhập tin nhắn tại đây") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White
                )
            )
            IconButton(onClick = {
                if (messageText.isNotBlank()) {
                    viewModel.sendMessage(
                        patientId,
                        Message(id = "", sender = "doctor", content = messageText, timestamp = "")
                    )
                    messageText = ""
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val isDoctor = message.sender == "doctor"
    val bubbleColor = if (isDoctor) Color(0xFFD0E8FF) else Color(0xFFECECEC)
    val alignment = if (isDoctor) Arrangement.End else Arrangement.Start
    val avatar = if (isDoctor) R.drawable.avatar_doctor else R.drawable.avatar_user

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = alignment,
        verticalAlignment = Alignment.Top
    ) {
        if (!isDoctor) {
            Image(
                painter = painterResource(id = avatar),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(bubbleColor)
                .padding(12.dp)
        ) {
            Text(text = message.content)
        }

        if (isDoctor) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = avatar),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
            )
        }
    }
}
