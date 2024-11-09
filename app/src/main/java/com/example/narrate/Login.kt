package com.example.narrate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun LoginScreen(
    authManager: AuthManager,
    onLoginSuccess: (String) -> Unit,
    navController: NavController,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var wrongPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var networkError by remember { mutableStateOf(false) }
    var showCapsWarning by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var invalidEmail by remember { mutableStateOf(false) }
    var emailExists by remember { mutableStateOf(false) }

    GradientOverlayScreen(imageRes = R.drawable.narrate) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(30.dp)
                .padding(top = 60.dp)
        ) {
            Text(
                "Enter your credentials",
                style = TextStyle(fontSize = 50.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                "Login to account with your registered email and continue your journey",
                modifier = Modifier.padding(10.dp)
            )

            Spacer(Modifier.height(50.dp))

            // Email Field
            Column {
                // Check if email is empty or invalid
                val isEmailEmpty = email.isEmpty()
                val isEmailInvalid = !isEmailEmpty && !isValidEmail(email)

                // Show error message if email is invalid and not empty
                if (isEmailInvalid) {
                    Text("Invalid email format", color = Color.Red)
                }

                EmailTextField(
                    value = email,
                    onValueChange = {
                        emailExists = false
                        email = it // Update the email state
                    },
                    leadingIcon = { Icon(Icons.Rounded.Email, contentDescription = null) },
                    placeholder = "Email"
                )
            }

            Spacer(Modifier.height(10.dp))

            // Password Field
            Column {
                if (wrongPassword) {
                    Text("Wrong password. Please try again.", color = Color.Red)
                }
                PasswordTextField(
                    value = password,
                    onValueChange = {
                        password = it; wrongPassword = false; showCapsWarning =
                        it.any { char -> char.isUpperCase() } // Check for uppercase letters
                    },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
                    placeholder = "Enter your password"
                )

            }

            Spacer(Modifier.height(50.dp))

            // Display network error message
            if (networkError) {
                Text("Network error. Please check your connection.", color = Color.Red)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                Text("Remember Me", modifier = Modifier.padding(start = 8.dp))
            }

            Button(modifier = Modifier
                .width(300.dp)
                .height(55.dp), onClick = {
                isLoading = true // Set loading state when button is clicked

                // Check for network availability before logging in
                if (isNetworkAvailable(context)) {
                    if (isValidEmail(email)) { // Validate email before attempting login
                        authManager.login(email, password) { success, error ->
                            isLoading = false // Reset loading state after login attempt

                            if (success) {
                                // Fetch user data after successful login
                                authManager.fetchUserData { username ->
                                    if (username == null) {
                                        navController.navigate("naming") // Navigate to naming screen if username is missing
                                    } else {
                                        // Save credentials securely if Remember Me is checked
                                        if (rememberMe) {
                                            // Implement secure storage logic here
                                        }
                                        // UserPreferences(context).saveUserName(username.toString())
                                        // navController.navigate("home/$username")
                                    }
                                }
                            } else {
                                wrongPassword = true
                            }
                        }
                    } else {
                        invalidEmail = true // Set invalid email flag if check fails
                        isLoading = false // Reset loading state since login won't proceed
                    }
                } else {
                    isLoading = false
                    networkError = true // Set network error state to true
                }
            }) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row {
                Text("If you're new to our service ", style = TextStyle(color = Color.White))
                Text(
                    "Sign up here",
                    style = TextStyle(
                        textDecoration = TextDecoration.Underline,
                        color = Color.White
                    ),
                    modifier = Modifier.clickable { navController.navigate("signup") })
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
