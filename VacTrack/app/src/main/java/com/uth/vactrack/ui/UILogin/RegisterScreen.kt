package com.uth.vactrack.ui.UILogin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uth.vactrack.R
import com.uth.vactrack.config.AppConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.remember
import androidx.compose.foundation.rememberScrollState

@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val blue = Color(0xFF1976D2)

    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6
    val isConfirmPasswordValid = password == confirmPassword && confirmPassword.isNotEmpty()
    val isNameValid = name.isNotBlank()
    val isFullNameValid = fullName.isNotBlank()
    val isAgeValid = age.toIntOrNull()?.let { it in 1..120 } == true
    val isDobValid = dob.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))
    val isAddressValid = address.isNotBlank()
    val isPhoneValid = phone.length in 8..15
    val canRegister = isEmailValid && isPasswordValid && isConfirmPasswordValid && isNameValid && isFullNameValid && isAgeValid && isDobValid && isAddressValid && isPhoneValid && !loading

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun register() {
        loading = true
        error = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/auth/register")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                val nameFromFullName = fullName.trim().substringAfterLast(' ')
                val body = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                    put("name", nameFromFullName)
                    put("fullName", fullName)
                    put("age", age.toInt())
                    put("dob", dob)
                    put("address", address)
                    put("phone", phone)
                    put("role", "user")
                }.toString()
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                withContext(Dispatchers.Main) {
                    loading = false
                    if (conn.responseCode == 200) {
                        onRegisterSuccess(json.getString("token"))
                        showToast(json.getString("message"))
                    } else {
                        error = json.optString("message", "Đăng ký thất bại")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loading = false
                    error = e.localizedMessage ?: "Đăng ký thất bại"
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
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_vactrack_logo),
                contentDescription = "Logo VacTrack",
                modifier = Modifier.size(220.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Create Account",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp, color = blue),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Sign up to get started!",
                style = TextStyle(fontSize = 15.sp, color = Color(0xFF888888)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(22.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; if (error != null) error = null },
                label = { Text("Email") },
                singleLine = true,
                isError = email.isNotBlank() && !isEmailValid,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_email), contentDescription = null, tint = blue) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; if (error != null) error = null },
                label = { Text("Password") },
                singleLine = true,
                isError = password.isNotBlank() && !isPasswordValid,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_password), contentDescription = null, tint = blue) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; if (error != null) error = null },
                label = { Text("Confirm Password") },
                singleLine = true,
                isError = confirmPassword.isNotBlank() && !isConfirmPasswordValid,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_password), contentDescription = null, tint = blue) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it; if (error != null) error = null },
                label = { Text("Full Name") },
                singleLine = true,
                isError = fullName.isNotBlank() && !isFullNameValid,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_person), contentDescription = null, tint = blue) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it.filter { c -> c.isDigit() }; if (error != null) error = null },
                    label = { Text("Age") },
                    singleLine = true,
                    isError = age.isNotBlank() && !isAgeValid,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    leadingIcon = { Icon(painterResource(id = R.drawable.ic_cake), contentDescription = null, tint = blue) }
                )
                OutlinedTextField(
                    value = dob,
                    onValueChange = { dob = it; if (error != null) error = null },
                    label = { Text("DOB (dd-MM-yyyy)") },
                    singleLine = true,
                    isError = dob.isNotBlank() && !isDobValid,
                    modifier = Modifier.weight(2f),
                    shape = RoundedCornerShape(14.dp),
                    leadingIcon = { Icon(painterResource(id = R.drawable.ic_calendar), contentDescription = null, tint = blue) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = address,
                onValueChange = { address = it; if (error != null) error = null },
                label = { Text("Address") },
                singleLine = true,
                isError = address.isNotBlank() && !isAddressValid,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_home), contentDescription = null, tint = blue) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it.filter { c -> c.isDigit() }; if (error != null) error = null },
                label = { Text("Phone") },
                singleLine = true,
                isError = phone.isNotBlank() && !isPhoneValid,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_phone), contentDescription = null, tint = blue) }
            )
            if (error != null) {
                Text(error!!, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = { register() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = canRegister,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canRegister) blue else Color(0xFFB0B8E6)
                ),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                if (loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                else Text("Register", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    "Sign In",
                    color = blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onBack() }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
} 