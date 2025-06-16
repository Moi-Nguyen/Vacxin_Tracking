package com.uth.vactrack.ui.UILogin

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmResetScreen(
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val blue = Color(0xFF1976D2)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = com.uth.vactrack.R.drawable.ic_arrow_back),
                        contentDescription = "Back"
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = com.uth.vactrack.R.drawable.ic_vactrack_logo),
                contentDescription = "Logo VacTrack",
                modifier = Modifier.size(240.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Password reset",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your password has been successfully reset. click confirm to set a new password",
                style = TextStyle(fontSize = 14.sp, color = Color(0xFF888888)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = blue)
            ) {
                Text("Confirm", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
} 