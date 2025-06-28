package com.uth.vactrack.ui.UIUser

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun UserNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "main") {

        composable("main") {
            MainScreen(navController = navController)
        }

        composable("appointment") {
            AppointmentScreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        composable("select_service") {
            SelectServiceScreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        // Select Time and Slot
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

        // Payment
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

            PaymentScreen(
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

        // Booking Success ✅
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

            BookingSuccessScreen(
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

        // ✅ Profile screen
        composable("profile") {
            ProfileScreen(onBack = { navController.popBackStack() })
        }
    }
}
