package com.example.narrate

import android.app.Activity
import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
@Composable
fun AppBackHandler(onBackPressed: () -> Unit) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val callback = rememberUpdatedState(onBackPressed)

    DisposableEffect(Unit) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                callback.value()
            }
        }
        backDispatcher?.addCallback(callback)
        onDispose { callback.remove() }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    modifier: Modifier= Modifier,
    authManager: AuthManager,
    userSession: UserSession? // Accept user session as a parameter
) {
    val navController= rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController= navController,
        startDestination= if (userSession != null) "home/${userSession.username}/${userSession.date}/${userSession.imageResId}" else "login"
    ) {
        composable("login") {
            LoginScreen(authManager, onLoginSuccess= { username, date, imageResId ->
                navController.navigate("home/$username/$date/$imageResId")
            }, navController)
        }
        composable("signup") { SignUpScreen(authManager, onSignUpSuccess={ navController.navigate("welcome")}, navController)}
        composable("welcome") { WelcomeScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("personalize") { PersonalizeScreen(navController, context)}

        // Home screen route with parameters
        composable("home/{username}/{date}/{imageResId}") { backStackEntry ->
            val args= backStackEntry.arguments ?: return@composable
            val usernameArg= args.getString("username") ?: ""
            val dateArg= args.getString("date") ?: LocalDate.now().toString()
            val imageResIdArgString= args.getString("imageResId") ?: "0"

            val imageResIdArgInt= imageResIdArgString.toIntOrNull() ?: R.drawable.ocean

            HomeScreen(navController, usernameArg, dateArg, imageResIdArgInt)
        }

        // Profile screen route without parameters
        composable("profile") {
            ProfileScreen(navController, authManager)
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun NavigationGraph(
//    modifier: Modifier = Modifier,
//    authManager: AuthManager,
//    userSession: UserSession? // Accept user session as a parameter
//) {
//    val navController = rememberNavController()
//    val context = LocalContext.current
//
//    NavHost(
//        navController = navController,
//        startDestination = if (userSession != null) "home/${userSession.username}/${userSession.date}/${userSession.imageResId}" else "login"
//    ) {
//        composable("login") {
//            LoginScreen(authManager, onLoginSuccess = { username, date, imageResId ->
//                // Navigate to home with fetched data
//                navController.navigate("home/$username/$date/$imageResId")
//            }, navController)
//        }
//        composable("signup") {
//            SignUpScreen(authManager, onSignUpSuccess = { navController.navigate("welcome") }, navController)
//        }
//        composable("welcome") { WelcomeScreen(navController) }
//        composable("onboarding") { OnboardingScreen(navController) }
//        composable("personalize") { PersonalizeScreen(navController, context) }
//
//        // Home screen route with parameters
//        composable("home/{username}/{date}/{imageResId}") { backStackEntry ->
//            val args = backStackEntry.arguments ?: return@composable
//            val usernameArg = args.getString("username") ?: ""
//            val dateArg = args.getString("date") ?: LocalDate.now().toString()
//            val imageResIdArgString = args.getString("imageResId") ?: "0"
//
//            val imageResIdArgInt = imageResIdArgString.toIntOrNull() ?: R.drawable.ocean // Default placeholder if parsing fails
//
//            HomeScreen(navController, usernameArg, dateArg, imageResIdArgInt)
//        }
//        composable("profile") {
//            ProfileScreen(navController, authManager)
//        }
//    }
//}
