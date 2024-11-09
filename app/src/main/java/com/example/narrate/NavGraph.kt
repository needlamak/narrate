package com.example.narrate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    authManager: AuthManager
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    NavHost(
        navController = navController,
        startDestination = "signup"
//        if (username != null) "home/$username" else "first"
    ) {
        composable("login") {
            LoginScreen(authManager, onLoginSuccess = {
//                enteredName ->
//              //  userPreferences.saveUserName(enteredName)
//                navController.navigate("home/$enteredName") {
//                    popUpTo("login") { inclusive = true }
//                }
            }, navController)
        }

        composable("signup") {
            SignUpScreen(authManager, onSignUpSuccess = {
//                enteredEmail ->
//                userPreferences.saveUserName(enteredEmail)
//                navController.navigate("naming")
            }, navController)
        }
    }
}