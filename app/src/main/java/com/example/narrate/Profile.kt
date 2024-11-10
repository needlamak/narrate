package com.example.narrate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController, authManager: AuthManager) {
    // State variables for user data
    var username by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var imageResId by remember { mutableStateOf(R.drawable.ocean) } // Default placeholder

    // Fetch user data when the screen is displayed
    LaunchedEffect(Unit) {
        authManager.fetchUserData { fetchedUsername, fetchedDate, fetchedImageResId ->
            username = fetchedUsername ?: "Unknown User"
            date = fetchedDate ?: "No Date Selected"
            imageResId = fetchedImageResId ?: R.drawable.ocean // Default placeholder if parsing fails
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            item {
                Text("Username", style = MaterialTheme.typography.bodySmall)
                Text(username, style = MaterialTheme.typography.bodyMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
            item {
                //Text("Date", style = MaterialTheme.typography.d)
                Text(date, style = MaterialTheme.typography.bodyMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
            item {
                Text("Profile Image", style = MaterialTheme.typography.bodyMedium)
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // Sign Out Button
            item {
                Button(
                    onClick = {
                        authManager.signOut()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true } // Clear back stack
                        }
                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text("Sign Out")
                }
            }
        }
    }
}