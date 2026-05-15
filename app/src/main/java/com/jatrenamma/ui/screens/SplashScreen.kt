package com.jatrenamma.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jatrenamma.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val alpha  = remember { Animatable(0f) }
    val scale  = remember { Animatable(0.7f) }

    LaunchedEffect(Unit) {
        // Animate in
        launch { alpha.animateTo(1f,  tween(700)) }
        launch { scale.animateTo(1f,  spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
        delay(2200)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(listOf(JatreBlue, JatreBlueDark))
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative ring
        Box(
            modifier = Modifier
                .size(260.dp)
                .background(JatreGold.copy(alpha = 0.06f), androidx.compose.foundation.shape.CircleShape)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(JatreGold.copy(alpha = 0.08f), androidx.compose.foundation.shape.CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            Text("🪔", fontSize = 80.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(12.dp))
            Text(
                "ಜಾತ್ರೆ",
                style = MaterialTheme.typography.displayLarge,
                color = JatreGold,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "NAMMA PRIDE",
                color = JatreSilver,
                letterSpacing = 5.sp,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Your Digital Guide to the Village Fair",
                color = JatreSilver.copy(alpha = 0.7f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
