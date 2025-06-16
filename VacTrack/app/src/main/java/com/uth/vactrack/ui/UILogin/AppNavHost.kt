package com.uth.vactrack.ui.UILogin

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    var loginEmail by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { /* TODO: Điều hướng sang màn khác */ },
                onSignUpClick = { navController.navigate("register") },
                onForgotPassword = { email ->
                    navController.navigate("forgot_password?email=$email")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    // Sau khi đăng ký thành công, quay về login
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "forgot_password?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ForgotPasswordScreen(
                initialEmail = email,
                onBack = { navController.popBackStack() },
                onRequestResetSuccess = { sentEmail ->
                    navController.navigate("otp?email=$sentEmail")
                }
            )
        }
        composable(
            "otp?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OtpScreen(
                email = email,
                onOtpVerified = { resetToken ->
                    navController.navigate("confirm_reset?resetToken=$resetToken")
                },
                onResend = {
                    // Có thể popBackStack về forgot_password hoặc gọi lại API gửi OTP
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "confirm_reset?resetToken={resetToken}",
            arguments = listOf(navArgument("resetToken") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val resetToken = backStackEntry.arguments?.getString("resetToken") ?: ""
            ConfirmResetScreen(
                onConfirm = {
                    navController.navigate("set_new_password?resetToken=$resetToken")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "set_new_password?resetToken={resetToken}",
            arguments = listOf(navArgument("resetToken") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val resetToken = backStackEntry.arguments?.getString("resetToken") ?: ""
            SetNewPasswordScreen(
                resetToken = resetToken,
                onPasswordReset = {
                    // Sau khi đặt lại mật khẩu thành công, quay về login
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
} 