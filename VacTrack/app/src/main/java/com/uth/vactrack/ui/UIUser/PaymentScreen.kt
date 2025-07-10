package com.uth.vactrack.ui.UIUser

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.uth.vactrack.R
import com.uth.vactrack.ui.viewmodel.PaymentViewModel
import com.uth.vactrack.ui.viewmodel.SharedViewModel
import com.uth.vactrack.ui.UIUser.BottomNavigationBar
import java.net.URLEncoder
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    serviceName: String,
    selectedDate: String,
    selectedTime: String,
    bill: Int,
    navController: NavController,
    onBack: () -> Unit = {},
    onCancel: () -> Unit = {},
    onPay: () -> Unit = {},
    paymentViewModel: PaymentViewModel = viewModel(),
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val state by paymentViewModel.state.collectAsStateWithLifecycle()
    // val navController = androidx.navigation.compose.rememberNavController() // XÓA DÒNG NÀY

    // Handle success
    LaunchedEffect(state.success) {
        if (state.success) {
            Toast.makeText(context, state.message ?: "Thanh toán thành công", Toast.LENGTH_SHORT).show()
            paymentViewModel.resetSuccess()
            navController.navigate("home") {
                popUpTo(0)
            }
        }
    }

    // Handle error
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            paymentViewModel.clearError()
        }
    }

    // QR Info
    val bankId = "ACB"
    val accountNumber = "39598507"
    val template = "compact2"
    val accountName = "Nguyen Duc Luong"
    val amount = bill * 1000
    val addInfo = "thanh toan Vactrack voi so tien ${amount}đ"
    val encodedInfo = URLEncoder.encode(addInfo, "UTF-8")
    val encodedName = URLEncoder.encode(accountName, "UTF-8")
    val qrUrl = "https://img.vietqr.io/image/$bankId-$accountNumber-$template.png" +
            "?amount=$amount&addInfo=$encodedInfo&accountName=$encodedName"

    val sharedState = sharedViewModel.sharedState.collectAsStateWithLifecycle().value
    val user = sharedState.currentUser
    val token: String? = sharedState.token
    val serviceId: String? = sharedState.selectedServiceId
    val facilityId: String? = sharedState.selectedFacilityId

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_logo_xoanen),
                            contentDescription = "Logo",
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step progress
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
                                if (index <= 2) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Info card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                            val userName = user?.fullName ?: user?.name ?: "Guest"
                            Text(userName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Service: $serviceName", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("Facility: Gia Dinh Hospital", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_calendar),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("$selectedDate   $selectedTime", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Hospital bill: ${bill}.000đ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // QR Code
            AsyncImage(
                model = qrUrl,
                contentDescription = "QR VietQR",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(450.dp)
                    .height(420.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel", fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        val safeToken = token
                        val safeServiceId = serviceId
                        val safeFacilityId = facilityId
                        Log.d("DEBUG_BOOKING", "user=$user, token=$safeToken, serviceId=$safeServiceId, facilityId=$safeFacilityId")
                        Toast.makeText(context, "user=$user\ntoken=$safeToken\nserviceId=$safeServiceId\nfacilityId=$safeFacilityId", Toast.LENGTH_LONG).show()
                        if (user != null && !safeToken.isNullOrBlank() && !safeServiceId.isNullOrBlank() && !safeFacilityId.isNullOrBlank()) {
                            val bookingRequest = com.uth.vactrack.data.model.BookingRequest(
                                userId = user.id,
                                serviceId = safeServiceId,
                                facilityId = safeFacilityId,
                                date = selectedDate,
                                time = selectedTime,
                                doseNumber = 1,
                                notes = null
                            )
                            paymentViewModel.bookAppointment(safeToken, bookingRequest) {
                                // Xử lý kết quả nếu cần
                            }
                        } else {
                            Toast.makeText(context, "Thiếu thông tin đặt lịch!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(2f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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