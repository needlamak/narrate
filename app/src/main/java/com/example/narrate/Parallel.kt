package com.example.narrate

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val screen: @Composable () -> Unit
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    // Define screens with their respective icons
    val screens = listOf(
        BottomNavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            screen = { HomeScreen() }
        ),
        BottomNavigationItem(
            title = "Discover",
            icon = Icons.Default.Search,
            screen = { DiscoverScreen() }
        ),
        BottomNavigationItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            screen = { SettingsScreen() }
        )
    )

    val pagerState = rememberPagerState(pageCount = { screens.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            screens[page].screen()
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Home Screen")
    }
}

@Composable
fun DiscoverScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Discover Screen")
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Settings Screen")
    }
}

// Preview the MainScreen composable
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

// Optional: Add smooth scrolling between pages
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithScrollBehavior() {
    val screens = listOf(
        BottomNavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            screen = { HomeScreen() }
        ),
        BottomNavigationItem(
            title = "Discover",
            icon = Icons.Default.Search,
            screen = { DiscoverScreen() }
        ),
        BottomNavigationItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            screen = { SettingsScreen() }
        )
    )

    val pagerState = rememberPagerState(pageCount = { screens.size })
    val scope = rememberCoroutineScope()

    // Add scroll behavior for smooth transitions
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            NavigationBar {
                screens.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                // Smooth scroll to page
                                pagerState.animateScrollToPage(
                                    page = index,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            screens[page].screen()
        }
    }
}

// Preview the MainScreenWithScrollBehavior composable
@Preview(showBackground = true)
@Composable
fun MainScreenWithScrollBehaviorPreview() {
    MainScreenWithScrollBehavior()
}