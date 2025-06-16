package com.uth.vactrack.ui.UILogin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uth.vactrack.config.AppConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SetNewPasswordScreen(
    resetToken: String,
    onPasswordReset: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val blue = Color(0xFF1976D2)
    val canUpdate = password.length >= 6 && password == confirmPassword && !loading

    fun updatePassword() {
        loading = true
        error = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/auth/set-new-password")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Authorization", "Bearer $resetToken")
                conn.doOutput = true
                val body = JSONObject().apply {
                    put("newPassword", password)
                }.toString()
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                withContext(Dispatchers.Main) {
                    loading = false
                    Toast.makeText(context, json.optString("message", "Password reset!"), Toast.LENGTH_SHORT).show()
                    onPasswordReset()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loading = false
                    error = e.localizedMessage ?: "Lỗi đặt lại mật khẩu"
                }
            }
        }
    }

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
                text = "Set a new password",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Create a new password. Ensure it differs from previous ones for security",
                style = TextStyle(fontSize = 14.sp, color = Color(0xFF888888)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Password", fontWeight = FontWeight.Medium, fontSize = 15.sp, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (error != null) error = null
                },
                placeholder = { Text("Enter your new password") },
                singleLine = true,
                isError = password.isNotBlank() && password.length < 6,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue,
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = Color.Red,
                    focusedLabelColor = blue,
                    unfocusedLabelColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Confirm Password", fontWeight = FontWeight.Medium, fontSize = 15.sp, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (error != null) error = null
                },
                placeholder = { Text("Re-enter password") },
                singleLine = true,
                isError = confirmPassword.isNotBlank() && confirmPassword != password,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF22306A),
                    unfocusedBorderColor = Color(0xFFB0B8E6),
                    errorBorderColor = Color.Red,
                    focusedLabelColor = Color(0xFF22306A),
                    unfocusedLabelColor = Color(0xFFB0B8E6)
                )
            )
            if (error != null) {
                Text(error!!, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { updatePassword() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = canUpdate,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canUpdate) blue else Color(0xFFB0B8E6)
                )
            ) {
                if (loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                else Text("Update Password", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
} 