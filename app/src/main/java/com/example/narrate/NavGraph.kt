package com.example.narrate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    authManager: AuthManager
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("login") {
            LoginScreen(authManager, onLoginSuccess = {
                // You can handle any additional logic here if needed.
                navController.navigate("welcome")
            }, navController)
        }

        composable("signup") {
            SignUpScreen(authManager, onSignUpSuccess = {// email ->
                // You can handle any additional logic here if needed.
                navController.navigate("welcome")
            }, navController)
        }

        composable("welcome") { WelcomeScreen(navController) }

        composable("welcome") { OnboardingScreen(navController) }

//        composable("naming") {
//            // Assuming you have a NamingScreen Composable defined elsewhere.
//            NamingScreen(navController)
//        }

        // Add additional screens as needed...
    }
}