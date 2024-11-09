package com.example.narrate


import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalizeScreen(navController: NavController, context: Context) {
    var username by remember { mutableStateOf("") }
    var animationStarted by remember { mutableStateOf(false) }
    var showTextFieldAndButton by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) } // Store selected date
    var selectedImage by remember { mutableStateOf<Int?>(null) } // Store selected image resource ID

    LaunchedEffect(Unit) {
        delay(100) // Delay to ensure navigation is complete
        animationStarted = true
        delay(300) // Wait for image animation to finish before showing text field and button
        showTextFieldAndButton = true
    }

    val images = listOf(
        R.drawable.ocean,
        R.drawable.narrate,
        R.drawable.narration,
        R.drawable.fisherman,
        R.drawable.ocean
    )

    // Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Circular Image with Animation (for preview)
            AnimatedVisibility(
                visible = animationStarted,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                exit = fadeOut()
            ) {
                Image(
                    painter = painterResource(
                        id = selectedImage ?: R.drawable.ocean
                    ), // Placeholder or default image
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            // Image Selector below Username TextField
            ImageSelector(images, selectedImage) { newImage ->
                selectedImage = newImage // Update selected image when changed
            }

            Spacer(modifier = Modifier.height(30.dp))

            AnimatedVisibility(
                visible = showTextFieldAndButton,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = fadeOut()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Text Field for Name Input
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = { Text("Enter your username") },
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.White,
                        ),
                        modifier = Modifier
                            .fillMaxWidth(.75f)
                            .clip(RoundedCornerShape(10)),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    // Centered Date Picker below Username TextField
                    CenteredDatePicker(selectedDate) { newDate ->
                        selectedDate= newDate // Update selected date when changed
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (username.isNotBlank() && selectedImage != null) {
                                val authManager = AuthManager(context as MainActivity)
                                authManager.uploadUserName(username) { success, error ->
                                    if (success) {
                                        // Navigate to HomeScreen after successful upload with username and selected date and image resource ID
                                        navController.navigate("home/$username/${selectedDate.toString()}/$selectedImage")
                                    } else {
                                        Log.e(
                                            "PersonalizeScreen",
                                            "Failed to upload username: $error"
                                        )
                                    }
                                }
                            }
                        },
                        enabled = username.isNotBlank() && selectedImage != null,
                        modifier = Modifier.size(300.dp, 60.dp),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(
                            "Continue",
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageSelector(
    images: List<Int>, // List of drawable resource IDs
    selectedImage: Int?,
    onImageSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
    ) {
        items(images) { imageRes ->
            ImageItem(
                imageRes = imageRes,
                isSelected = selectedImage == imageRes,
                onClick = { onImageSelected(imageRes) }
            )
        }
    }
}

@Composable
fun ImageItem(
    imageRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color.Blue else Color.Transparent

    Box(
        modifier = Modifier
            .size(70.dp)
            .clickable(onClick = onClick)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray) // Background color for better visibility
            .padding(4.dp), // Padding inside the box
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
