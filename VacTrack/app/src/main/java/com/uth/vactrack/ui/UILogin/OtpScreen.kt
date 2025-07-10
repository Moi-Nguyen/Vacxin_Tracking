package com.uth.vactrack.ui.UILogin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uth.vactrack.R
import com.uth.vactrack.ui.viewmodel.OtpViewModel
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import android.os.Vibrator
import android.os.VibrationEffect
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState

@Composable
fun OtpScreen(
    email: String,
    onOtpVerified: (resetToken: String) -> Unit,
    onResend: () -> Unit,
    onBack: () -> Unit,
    viewModel: OtpViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val blue = Color(0xFF1976D2)
    val otpLength = 6
    var otpInputs by remember { mutableStateOf(List(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val focusManager = LocalFocusManager.current

    // Focus ô đầu tiên khi vào màn hình
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            // Rung khi sai mã
            val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
            vibrator?.let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    it.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(200)
                }
            }
            viewModel.clearError()
        }
    }
    LaunchedEffect(state.success) {
        if (state.success) {
            val token = state.resetToken
            if (token != null) {
                onOtpVerified(token)
                viewModel.resetSuccess()
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
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back"
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.img_logo_xoanen),
                contentDescription = "Logo VacTrack",
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Check your email",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "We sent a reset link to $email\nenter 6 digit code that mentioned in the email",
                style = TextStyle(fontSize = 14.sp, color = Color(0xFF888888)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (i in 0 until otpLength) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isFocused = interactionSource.collectIsFocusedAsState().value
                    OutlinedTextField(
                        value = otpInputs[i],
                        onValueChange = { newValue: String ->
                            if (newValue.length <= 1 && (newValue.isEmpty() || newValue.all { c: Char -> c.isDigit() })) {
                                val newInputs = otpInputs.toMutableList()
                                newInputs[i] = newValue
                                otpInputs = newInputs
                                val fullOtp = otpInputs.joinToString("")
                                println("DEBUG OTP INPUT: $fullOtp")
                                viewModel.setOtp(fullOtp)
                                if (newValue.isNotEmpty() && i < otpLength - 1) {
                                    focusRequesters[i + 1].requestFocus()
                                }
                                if (newValue.isEmpty() && i > 0) {
                                    focusRequesters[i - 1].requestFocus()
                                }
                            }
                        },
                        modifier = Modifier
                            .width(48.dp)
                            .height(56.dp)
                            .focusRequester(focusRequesters[i]),
                        shape = RoundedCornerShape(16.dp),
                        textStyle = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        maxLines = 1,
                        isError = state.error != null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { 
                    println("DEBUG VERIFY BUTTON CLICKED")
                    println("DEBUG EMAIL: $email")
                    println("DEBUG OTP INPUTS: $otpInputs")
                    viewModel.verifyOtp(email) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = otpInputs.all { it.isNotEmpty() } && !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (otpInputs.all { it.isNotEmpty() }) blue else blue
                )
            ) {
                if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                else Text("Verify Code", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Haven't got the email yet? ",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                TextButton(onClick = onResend, contentPadding = PaddingValues(0.dp)) {
                    Text(
                        text = "Resend email",
                        color = blue,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
} 