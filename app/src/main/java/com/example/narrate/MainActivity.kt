package com.example.narrate

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.narrate.ui.theme.NarrateTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        authManager = AuthManager(this)

        val sessionManager = SessionManager(this)
        val userSession = sessionManager.getUserSession()

        enableEdgeToEdge()
        setContent {
            NarrateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationGraph(
                        modifier = Modifier.padding(innerPadding),
                        authManager,
                        userSession // Pass the user session to the NavigationGraph
                    )
                }
            }
        }
    }
}