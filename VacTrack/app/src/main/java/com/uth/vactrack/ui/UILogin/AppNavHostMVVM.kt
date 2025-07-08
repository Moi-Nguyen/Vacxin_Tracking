package com.uth.vactrack.ui.UILogin

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.uth.vactrack.ui.UIUser.*
import com.uth.vactrack.ui.viewmodel.SharedViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavHostMVVM(
    startDestination: String = "login",
    sharedViewModel: SharedViewModel = viewModel()
) {
    val navController = rememberNavController()
    val sharedState by sharedViewModel.sharedState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreenMVVM(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate("register") },
                onForgotPassword = { email ->
                    navController.navigate("forgot_password?email=$email")
                }
            )
        }

        composable("register") {
            RegisterScreenMVVM(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "forgot_password?email={email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ForgotPasswordScreenMVVM(
                initialEmail = email,
                onBack = { navController.popBackStack() },
                onRequestResetSuccess = { sentEmail ->
                    navController.navigate("otp?email=$sentEmail")
                }
            )
        }

        composable(
            "otp?email={email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OtpScreenMVVM(
                email = email,
                onOtpVerified = { resetToken ->
                    navController.navigate("confirm_reset?resetToken=$resetToken")
                },
                onResend = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "confirm_reset?resetToken={resetToken}",
            arguments = listOf(navArgument("resetToken") {
                type = NavType.StringType
                defaultValue = ""
            })
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
            arguments = listOf(navArgument("resetToken") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val resetToken = backStackEntry.arguments?.getString("resetToken") ?: ""
            SetNewPasswordScreenMVVM(
                resetToken = resetToken,
                onPasswordReset = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreenMVVM(navController = navController)
        }

        composable("main") {
            MainScreenMVVM(navController = navController)
        }

        composable("appointment") {
            AppointmentScreenMVVM(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        composable("select_service") {
            SelectServiceScreenMVVM(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "select_time_and_slot/{serviceName}/{bill}",
            arguments = listOf(
                navArgument("serviceName") { type = NavType.StringType },
                navArgument("bill") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
            val bill = backStackEntry.arguments?.getInt("bill") ?: 0
            SelectTimeAndSlotScreen(
                serviceName = serviceName,
                bill = bill,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "payment/{service}/{date}/{time}/{bill}",
            arguments = listOf(
                navArgument("service") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType },
                navArgument("bill") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val service = URLDecoder.decode(
                backStackEntry.arguments?.getString("service") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val date = URLDecoder.decode(
                backStackEntry.arguments?.getString("date") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val time = URLDecoder.decode(
                backStackEntry.arguments?.getString("time") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val bill = backStackEntry.arguments?.getInt("bill") ?: 0

            PaymentScreenMVVM(
                serviceName = service,
                selectedDate = date,
                selectedTime = time,
                bill = bill,
                onBack = { navController.popBackStack() },
                onCancel = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onPay = {
                    val encodedService = URLEncoder.encode(service, StandardCharsets.UTF_8.toString())
                    val encodedDate = URLEncoder.encode(date, StandardCharsets.UTF_8.toString())
                    val encodedTime = URLEncoder.encode(time, StandardCharsets.UTF_8.toString())
                    navController.navigate("booking_success/$encodedService/$encodedDate/$encodedTime/$bill")
                }
            )
        }

        composable(
            "booking_success/{service}/{date}/{time}/{bill}",
            arguments = listOf(
                navArgument("service") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType },
                navArgument("bill") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val service = URLDecoder.decode(
                backStackEntry.arguments?.getString("service") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val date = URLDecoder.decode(
                backStackEntry.arguments?.getString("date") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val time = URLDecoder.decode(
                backStackEntry.arguments?.getString("time") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val bill = backStackEntry.arguments?.getInt("bill") ?: 0

            BookingSuccessScreenMVVM(
                serviceName = service,
                selectedDate = date,
                selectedTime = time,
                bill = bill,
                onFinish = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreenMVVM(navController = navController)
        }

        composable("edit_profile") {
            EditProfileScreenMVVM(
                onBack = { navController.popBackStack() },
                onSubmit = { navController.popBackStack() }
            )
        }

        composable("tracking_booking") {
            TrackingBookingScreenMVVM(navController = navController)
        }
    }
} 