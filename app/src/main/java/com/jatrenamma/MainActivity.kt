package com.jatrenamma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.jatrenamma.data.Screen
import com.jatrenamma.ui.components.JatreBottomNav
import com.jatrenamma.ui.components.JatreHeader
import com.jatrenamma.ui.screens.*
import com.jatrenamma.ui.theme.JatreNammaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ensure Firebase is initialized with the SAME dummy key as JatreApplication
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApiKey(JatreApplication.DUMMY_KEY)
                    .setApplicationId("1:1234567890:android:abcdef123456")
                    .setProjectId("dummy-project-id")
                    .build()
                FirebaseApp.initializeApp(this, options)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            JatreNammaTheme {
                JatreApp()
            }
        }
    }
}

@Composable
fun JatreApp() {
    var showSplash   by remember { mutableStateOf(true) }
    var isLoggedIn   by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    val isOffline    = false 

    if (showSplash) {
        SplashScreen(onFinished = { showSplash = false })
        return
    }

    if (!isLoggedIn) {
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
        return
    }

    Scaffold(
        topBar    = { JatreHeader() },
        bottomBar = {
            JatreBottomNav(current = currentScreen, onNavigate = { currentScreen = it })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    (slideInHorizontally { it / 4 } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it / 4 } + fadeOut())
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    Screen.HOME       -> HomeScreen(isOffline = isOffline)
                    Screen.SCHEDULE   -> ScheduleScreen(isOffline = isOffline)
                    Screen.LOST_FOUND -> LostFoundScreen(isOffline = isOffline)
                    Screen.MAP        -> MapScreen()
                    Screen.SAFETY     -> SafetyScreen(isOffline = isOffline)
                    Screen.STORIES    -> StoriesScreen()
                    Screen.LOGIN      -> LoginScreen(onLoginSuccess = { isLoggedIn = true })
                }
            }
        }
    }
}
