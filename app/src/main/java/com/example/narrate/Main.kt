package com.example.narrate

import android.app.Activity
import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate

//@Composable
//fun MainScreen(authManager: AuthManager) {
//
//    val tabs = listOf("Home", "Discover", "Chatlist", "Settings")
//    val pagerState = rememberPagerState(pageCount = {tabs.size})
//
//    Column {
//        TabRow(selectedTabIndex = pagerState.currentPage) {
//            tabs.forEachIndexed { index, title ->
//                Tab(
//                    selected = pagerState.currentPage == index,
//                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
//                    text = { Text(title) }
//                )
//            }
//        }
//
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier.weight(1f)
//        ) { page ->
//            when (page) {
//                0 -> HomeScreen() // Replace with your HomeScreen implementation
//                1 -> DiscoverScreen() // Replace with your DiscoverScreen implementation
//                2 -> ChatListScreen() // Replace with your ChatListScreen implementation
//                3 -> SettingsScreen(authManager) // Pass authManager to SettingsScreen if needed
//            }
//        }
//    }
//}