package ie.setu.initial_implementation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                onLoginClick = {
                    navController.navigate(Routes.HOME) {
                        // Clear back stack so user can't go back to login after authenticated
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onMedicationClick = { /* To be implemented */ },
                onActivityClick = { /* To be implemented */ },
                onEmergencyClick = { /* To be implemented */ },
                onContactsClick = { /* To be implemented */ },
                onNavigationItemClick = { /* To be implemented */ }
            )
        }
    }
}