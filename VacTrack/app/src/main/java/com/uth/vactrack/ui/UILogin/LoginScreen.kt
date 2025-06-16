package com.uth.vactrack.ui.UILogin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.CallbackManager
import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPassword: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val emailFocusRequester = remember { FocusRequester() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Google Sign-In setup
    val googleClientId = stringResource(id = R.string.google_client_id)
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(googleClientId)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken != null) {
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                onLoginSuccess(user?.uid ?: "")
                                Toast.makeText(context, "Đăng nhập Google thành công!", Toast.LENGTH_SHORT).show()
                            } else {
                                error = task.exception?.localizedMessage ?: "Google login thất bại"
                            }
                        }
                } else {
                    error = "Không nhận được ID token từ Google"
                }
            } catch (e: ApiException) {
                error = e.localizedMessage ?: "Google login thất bại"
            }
        } else {
            error = "Đăng nhập Google bị hủy"
        }
    }

    fun handleGoogleLogin() {
        try {
            // Sign out from Google before showing account picker
            googleSignInClient.signOut().addOnCompleteListener {
                googleLauncher.launch(googleSignInClient.signInIntent)
            }
        } catch (e: Exception) {
            error = "Lỗi khởi tạo đăng nhập Google: ${e.message}"
        }
    }

    // Facebook Login setup
    val callbackManager = remember { CallbackManager.Factory.create() }
    
    fun handleFacebookLogin() {
        // Sign out from Facebook before showing login dialog
        LoginManager.getInstance().logOut()
        
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            onLoginSuccess(user?.uid ?: "")
                            Toast.makeText(context, "Đăng nhập Facebook thành công!", Toast.LENGTH_SHORT).show()
                        } else {
                            error = task.exception?.localizedMessage ?: "Facebook login thất bại"
                        }
                    }
            }

            override fun onCancel() {
                Toast.makeText(context, "Đăng nhập Facebook bị hủy", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(context, "Lỗi đăng nhập Facebook: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
        
        LoginManager.getInstance().logInWithReadPermissions(context.findActivity(), listOf("email", "public_profile"))
    }

    // Validate
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6
    val canLogin = isEmailValid && isPasswordValid && !loading

    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun login() {
        loading = true
        error = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("${AppConfig.BASE_URL}/api/auth/login")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                val body = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }.toString()
                conn.outputStream.use { it.write(body.toByteArray()) }
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                withContext(Dispatchers.Main) {
                    loading = false
                    if (conn.responseCode == 200) {
                        onLoginSuccess(json.getString("token"))
                        showToast(json.getString("message"))
                    } else {
                        error = json.optString("message", "Login failed")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loading = false
                    error = e.localizedMessage ?: "Login failed"
                }
            }
        }
    }

    val blue = Color(0xFF1976D2)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.align(Alignment.Center) chưa cần thiết
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_vactrack_logo),
                    contentDescription = "Logo VacTrack",
                    modifier = Modifier.size(240.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sign in to your account",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF7B83C2),
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { handleGoogleLogin() },
                    colors = ButtonDefaults.buttonColors(containerColor =  Color.White),
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Google", color = Color(0xFF22306A), fontWeight = FontWeight.Medium)
                }
                Button(
                    onClick = { handleFacebookLogin() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "Facebook",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Facebook", color = Color.Black, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFE0E3EB))
                Text("  Or  ", color = Color(0xFFB0B8E6), fontSize = 14.sp)
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFE0E3EB))
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (error != null) error = null
                },
                label = { Text("Email") },
                singleLine = true,
                isError = email.isNotBlank() && !isEmailValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue,
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = Color.Red,
                    focusedLabelColor = blue,
                    unfocusedLabelColor = Color.Black
                ),
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (error != null) error = null
                },
                label = { Text("Password") },
                singleLine = true,
                isError = password.isNotBlank() && !isPasswordValid,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = if (passwordVisible) "Hide" else "Show",
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                    //.background(Color(0xFFF5F7FB), RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue,
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = Color.Red,
                    focusedLabelColor = blue,
                    unfocusedLabelColor = Color.Black
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onForgotPassword(email) }) {
                    Text("Forget Password?", color = blue, fontSize = 13.sp)
                }
            }
            if (error != null) {
                Text(error!!, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = canLogin,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canLogin) blue else Color(0xFFB0B8E6)
                ),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                if (loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                else Text("Log In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have account? ", color = Color.Black, fontSize = 14.sp)
                Text(
                    "Sign Up",
                    color = blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

fun Context.findActivity(): Activity = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> error("Activity not found")
}
