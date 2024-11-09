package com.example.narrate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.absoluteValue


@Composable
fun OnboardingScreen(
    navController: NavController
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val showButton = pagerState.currentPage == pagerState.pageCount - 1

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        HorizontalPager(
            state = pagerState,
            Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 50.dp),
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            Spacer(Modifier.height(20.dp))
            val scaleFactor = 1f - (0.2f * pageOffset.absoluteValue).coerceIn(0f, 0.2f)
            Box {
                OnboardingPager(
                    title = when (page) {
                        0 -> "Welcome"
                        1 -> "Features"
                        2 -> "Get Started"
                        3 -> "Finish"
                        else -> ""
                    },
                    description = when (page) {
                        0 -> "This is the first screen."
                        1 -> "Here are some features."
                        2 -> "Let's get you started."
                        3 -> "You're all set!"
                        else -> ""
                    },
                    painterResource(id = getImageResource(page)), // Use a function to get image
                    scaleFactor
                )
            }
        }
        Spacer(Modifier.height(20.dp))

        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn(animationSpec = tween(durationMillis = 3000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 10))
        ) {
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
                    navController.navigate("naming")
                }
            )
        }

        Spacer(Modifier.weight(.07f))

        AnimatedVisibility(
            visible = pagerState.currentPage < pagerState.pageCount - 1,
            enter = fadeIn(animationSpec = tween(durationMillis = 10)), // Fade-in animation
            exit = fadeOut(animationSpec = tween(durationMillis = 300)) // Fade-out animation
        ) {
            PageIndicator(
                currentPage = pagerState.currentPage,
                pageCount = pagerState.pageCount,
                pagerState
            )
        }
        Spacer(Modifier.weight(.09f))
    }
}


@Composable
fun OnboardingPager(
    title: String, description: String,
    image: Painter, scaleFactor: Float
) {
    Column(
        modifier = Modifier
            .padding(top = 15.dp)
            .graphicsLayer(scaleX = scaleFactor, scaleY = scaleFactor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp * 0.60).dp) // 75% of screen height
                .clip(RoundedCornerShape(20)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = TextStyle(
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

fun getImageResource(page: Int): Int {
    return when (page) {
        0 -> R.drawable.ocean
        1 -> R.drawable.fisherman // Assuming you have these resources
        2 -> R.drawable.narrate // Assuming you have these resources
        else -> R.drawable.narration // Placeholder for missing image
    }
}

@Composable
fun PageIndicator(currentPage: Int, pageCount: Int, pagerState: PagerState) {
    val activeColor = MaterialTheme.colorScheme.primary // Active color based on theme
    val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // Inactive color based on theme

    Row(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .size(300.dp, 60.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            // Calculate the offset for the current page
            val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction

            // Determine color and width based on offset
            val color = if (index == currentPage) activeColor else inactiveColor

            // Set minimum width for inactive indicators
            val width = when {
                index == currentPage -> 24.dp // Active indicator width
                pageOffset.absoluteValue.coerceIn(0f, 1f) < 1 -> 16.dp // Slightly larger than inactive
                else -> 8.dp // Minimum width for inactive indicators
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(width, 8.dp)
                    .background(
                        color,
                        shape = RoundedCornerShape(4.dp) // Rounded corners for the indicator
                    )
            )
        }
    }
}