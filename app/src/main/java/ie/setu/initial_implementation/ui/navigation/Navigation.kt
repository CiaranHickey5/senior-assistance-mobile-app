package ie.setu.initial_implementation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ie.setu.initial_implementation.ui.screens.HomeScreen
import ie.setu.initial_implementation.ui.screens.LoginScreen

// Define routes
object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { email ->
                    navController.navigate("${Routes.HOME}/$email") {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            "${Routes.HOME}/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            // Extract email from navigation arguments
            val email = backStackEntry.arguments?.getString("email") ?: "User"
            HomeScreen(
                email = email,
                onMedicationClick = { /* To be implemented */ },
                onActivityClick = { /* To be implemented */ },
                onEmergencyClick = { /* To be implemented */ },
                onContactsClick = { /* To be implemented */ },
                onNavigationItemClick = { /* To be implemented */ }
            )
        }
    }
}