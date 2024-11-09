package com.example.narrate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun WelcomeScreen(
    navController: NavController
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.narration),
            contentDescription = "Start bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Get the best of Gadgets",
                color = Color.White,
                style = TextStyle(
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 60.sp
                ),
                modifier = Modifier
            )
            Spacer(Modifier.fillMaxSize(.5f))
            GoCard(
                text = "Get Started",
                modifier = Modifier
                    .size(300.dp, 60.dp),
                colors = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(20),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                onClick = {
                    navController.navigate("onboarding")
                }
            )
        }
    }
}


@Composable
fun GoCard(
    text: String,
    style: TextStyle,
    modifier: Modifier,
    colors: Color,
    shape: RoundedCornerShape,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = style
            )
        }
    }
}
