package com.example.narrate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    authManager: AuthManager
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "personalize"
    ) {
        composable("login") {
            LoginScreen(authManager, onLoginSuccess = { navController.navigate("welcome")}, navController)}
        composable("signup") {
            SignUpScreen(authManager, onSignUpSuccess = { navController.navigate("welcome")}, navController)}
        composable("welcome") { WelcomeScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("personalize") { PersonalizeScreen(navController, context)}


        // Add additional screens as needed...
    }
}