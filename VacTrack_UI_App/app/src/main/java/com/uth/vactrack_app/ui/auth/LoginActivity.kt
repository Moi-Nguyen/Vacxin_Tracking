package com.uth.vactrack_app.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uth.vactrack_app.R
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
/*import androidx.compose.ui.text.input.KeyboardActions*/


class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen(viewModel = viewModel)
            }
        }
    }
}

// Màu sắc chuẩn theo ảnh
private val PrimaryBlue = Color(0xFF6366F1)
private val LightGray = Color(0xFFF8F9FA)
private val DarkGray = Color(0xFF374151)
private val BorderGray = Color(0xFFE5E7EB)
private val TextGray = Color(0xFF9CA3AF)
private val TextDark = Color(0xFF111827)
private val ShadowColor = Color(0x1A6366F1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    val isLoginSuccessful by viewModel.isLoginSuccessful.observeAsState(false)

    // Focus và keyboard controller
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Tự động focus vào email khi vào màn hình
    LaunchedEffect(Unit) {
        if (email.isEmpty()) {
            emailFocusRequester.requestFocus()
        }
    }

    // Side effect: Hiển thị lỗi
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
    // Side effect: Đăng nhập thành công -> ẩn bàn phím
    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful) {
            keyboardController?.hide()
            Toast.makeText(context, "Login successful! 🎉", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_vactrack_logo),
                contentDescription = "VacTrack Logo",
                modifier = Modifier
                    .size(220.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Tiêu đề
            Text(
                text = "Sign in to your account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Nút Facebook & Google
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SocialButton(
                    iconRes = R.drawable.ic_facebook,
                    text = "Facebook",
                    onClick = { /* TODO: Facebook login */ },
                    backgroundColor = LightGray,
                    contentColor = DarkGray,
                    modifier = Modifier.weight(1f)
                )
                SocialButton(
                    iconRes = R.drawable.ic_google,
                    text = "Google",
                    onClick = { /* TODO: Google login */ },
                    backgroundColor = LightGray,
                    contentColor = DarkGray,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Or Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = BorderGray)
                Text(
                    text = "  Or  ",
                    color = TextGray,
                    fontSize = 14.sp
                )
                Divider(modifier = Modifier.weight(1f), color = BorderGray)
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Email
            VacTrackTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "example@gmail.com",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                focusRequester = emailFocusRequester,
                onNext = { passwordFocusRequester.requestFocus() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Password
            VacTrackPasswordField(
                value = password,
                onValueChange = { password = it },
                placeholder = "••••••••",
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
                imeAction = ImeAction.Done,
                focusRequester = passwordFocusRequester,
                onDone = {
                    keyboardController?.hide()
                    viewModel.login(email, password)
                }
            )
            // Forgot Password
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot Password?",
                    fontSize = 14.sp,
                    color = TextGray,
                    modifier = Modifier.clickable {
                        if (email.isNotEmpty()) {
                            viewModel.forgotPassword(email)
                        } else {
                            Toast.makeText(context, "Please enter your email first", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Nút Log In
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(15.dp)),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    disabledContainerColor = PrimaryBlue.copy(alpha = 0.6f)
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Log In",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Sign Up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = TextDark, fontSize = 16.sp)) {
                        append("Don't have account? ")
                    }
                    withStyle(style = SpanStyle(color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                        append("Sign Up")
                    }
                }
                Text(
                    text = annotatedString,
                    modifier = Modifier.clickable { /* TODO: Navigate to Sign Up */ },
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SocialButton(
    iconRes: Int,
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(56.dp)
            .shadow(2.dp, RoundedCornerShape(15.dp)),
        shape = RoundedCornerShape(15.dp),
        color = backgroundColor,
        onClick = onClick,
        border = null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacTrackTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    focusRequester: FocusRequester? = null,
    onNext: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = TextGray,
                fontSize = 16.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BorderGray,
            unfocusedBorderColor = BorderGray,
            cursorColor = PrimaryBlue,
            focusedTextColor = TextDark,
            unfocusedTextColor = TextDark
        ),
       /* keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = { onNext?.invoke() },
            onDone = { onNext?.invoke() }
        ),*/
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacTrackPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    imeAction: ImeAction = ImeAction.Done,
    focusRequester: FocusRequester? = null,
    onDone: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = TextGray,
                fontSize = 16.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BorderGray,
            unfocusedBorderColor = BorderGray,
            cursorColor = PrimaryBlue,
            focusedTextColor = TextDark,
            unfocusedTextColor = TextDark
        ),
        /*visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = { onDone?.invoke() }
        ),*/
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityChange) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = TextGray
                )
            }
        },
        singleLine = true
    )
}
