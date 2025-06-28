package com.uth.vactrack.ui.UIUser

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.uth.vactrack.R
import com.uth.vactrack.ui.theme.VacTrackTheme
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    serviceName: String,
    selectedDate: String,
    selectedTime: String,
    bill: Int, // bill in thousands (e.g., 100 = 100.000đ)
    onBack: () -> Unit = {},
    onCancel: () -> Unit = {},
    onPay: () -> Unit = {}
) {
    val bankId = "ACB"
    val accountNumber = "39598507"
    val template = "compact2"
    val accountName = "Nguyen Duc Luong"
    val amount = bill * 1000 // Convert to VNĐ
    val addInfo = "thanh toan Vactrack voi so tien ${amount}đ"

    val encodedInfo = URLEncoder.encode(addInfo, "UTF-8")
    val encodedName = URLEncoder.encode(accountName, "UTF-8")

    val qrUrl = "https://img.vietqr.io/image/$bankId-$accountNumber-$template.png" +
            "?amount=$amount&addInfo=$encodedInfo&accountName=$encodedName"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_logo_xoanen),
                            contentDescription = "Logo",
                            modifier = Modifier.size(52.dp)
                        )
                        Text("Payment", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info),
                            contentDescription = "Info"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedIndex = 1)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .background(
                                if (index <= 2) Color(0xFF00BCD4) else Color.LightGray,
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Appointment Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Nguyen Van A", fontWeight = FontWeight.Bold, fontSize = 21.sp)
                            Text("Service: $serviceName", fontSize = 18.sp)
                            Text("Facility: Gia Dinh Hospital", fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_calendar),
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("$selectedDate   $selectedTime", fontSize = 17.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Hospital bill: ${bill}.000đ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF222222)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // QR Code
            AsyncImage(
                model = qrUrl,
                contentDescription = "QR VietQR",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(500.dp) // enlarged size
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel", fontSize = 16.sp)
                }
                Button(
                    onClick = onPay,
                    modifier = Modifier.weight(2f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                ) {
                    Text("Direct Payment", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_move),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PaymentScreenPreview() {
    VacTrackTheme {
        PaymentScreen(
            serviceName = "Outpatient Vaccination",
            selectedDate = "Tuesday, 13 May 2025",
            selectedTime = "9:00 AM",
            bill = 200
        )
    }
}
