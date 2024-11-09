package com.example.narrate

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.material3.Checkbox
import androidx.compose.ui.platform.LocalContext

@Composable
fun SignUpScreen(
    authManager: AuthManager,
    onSignUpSuccess: (String) -> Unit,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordsMatch by remember { mutableStateOf(true) }
    var emailExists by remember { mutableStateOf(false) }
    var passwordLengthValid by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var networkError by remember { mutableStateOf(false) }
    var showCapsWarning by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    val context = LocalContext.current

    GradientOverlayScreen(imageRes = R.drawable.narrate) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(30.dp)
                .padding(top = 30.dp)
        ) {
            Text(
                "Create Your Account",
                style = TextStyle(fontSize = 50.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                "New here? Create a new account in milliseconds",
                modifier = Modifier.padding(10.dp)
            )

            Spacer(Modifier.height(20.dp))

            // Email Field
            Column {
                val isEmailEmpty = email.isEmpty()
                val isEmailInvalid = !isEmailEmpty && !isValidEmail(email)

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

            Spacer(modifier = Modifier.height(10.dp))

            // Password Field
            Column {

                // Validate password length; should be false if empty
                passwordLengthValid = password.length >= 6

                if (!passwordLengthValid && password.isNotEmpty()) {
                    Text("Password should be at least 6 characters", color = Color.Red)
                }

                PasswordTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordsMatch = confirmPassword.isEmpty() || it == confirmPassword
                        // Update warnings based on current input
                        showCapsWarning = it.any { char -> char.isUpperCase() }
                        passwordLengthValid = it.length >= 6
                    },
                    placeholder = "Password",
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Confirm Password Field
            Column {
                if (!passwordsMatch) {
                    Text("Passwords do not match", color = Color.Red)
                }
                PasswordTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        passwordsMatch = it == password
                    },
                    placeholder = "Confirm Password",
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = "Confirm Password"
                        )
                    }
                )
            }

            // Email Exists Message
            if (emailExists) {
                Text("The email address is already in use by another account. Login instead", color = Color.Green)
            }
            // Display network error message
            if (networkError) {
                Text("Network error. Please check your connection.", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                Text("Remember Me", modifier = Modifier.padding(start = 8.dp))
            }

            Button(modifier = Modifier
                .width(300.dp)
                .height(55.dp), onClick = {
                if (password == confirmPassword && password.length >= 6 && isValidEmail(email)) {
                    isLoading = true

                    // Check for network availability before signing up
                    if (isNetworkAvailable(context)) {
                        authManager.signUp(email, password) { success, error ->
                            isLoading = false
                            if (success) {
                                // Save credentials securely if Remember Me is checked
                                if (rememberMe) {
                                    // Implement secure storage logic here
                                }
                                onSignUpSuccess(email)
                            } else {
                                emailExists = true
                            }
                        }
                    } else {
                        isLoading = false
                        networkError = true // Set network error state to true
                    }
                } else {
                    passwordsMatch = password == confirmPassword
                    passwordLengthValid = password.length >= 6;
                }
            }) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text("Continue")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.Center) {
                Text("If email already exists ", style = TextStyle(color = Color.White))
                ClickableText(
                    text = AnnotatedString("Login"),
                    style = TextStyle(
                        textDecoration = TextDecoration.Underline,
                        color = Color.White
                    ),
                    onClick = { navController.navigate("login") })
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
