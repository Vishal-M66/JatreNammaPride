package com.jatrenamma

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jatrenamma.data.FeedbackRepository
import com.jatrenamma.ui.screens.JatreTextField
import com.jatrenamma.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun FeedbackScreen(onBack: () -> Unit) {
    val scope   = rememberCoroutineScope()

    var name       by remember { mutableStateOf("") }
    var message    by remember { mutableStateOf("") }
    var rating     by remember { mutableStateOf(0) }
    var submitted  by remember { mutableStateOf(false) }
    var isLoading  by remember { mutableStateOf(false) }
    var errorMsg   by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(JatreBlueDark, JatreBlue, JatreBlueDark)))
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = JatreGold)
                }
                Text(
                    "Feedback",
                    color = JatreGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Success state ─────────────────────────────────────────
            AnimatedVisibility(
                visible = submitted,
                enter = fadeIn() + slideInVertically()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉", fontSize = 64.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Thank you for your feedback!",
                        color = JatreGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "We'll use it to make Jatre-Namma better.",
                        color = JatreSilver,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = JatreGold)
                    ) {
                        Text("Go Back", color = JatreBlueDark, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ── Form state ────────────────────────────────────────────
            AnimatedVisibility(visible = !submitted) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorder, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        Text(
                            "Share your experience 🪔",
                            color = JatreGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Your feedback helps us improve.",
                            color = JatreSilver,
                            fontSize = 13.sp
                        )

                        Spacer(Modifier.height(20.dp))

                        // Name field
                        JatreTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Your Name (optional)",
                            placeholder = "e.g. Rakshitha",
                            leadingIcon = Icons.Filled.Person
                        )

                        Spacer(Modifier.height(14.dp))

                        // Message field
                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text("Your Feedback", color = JatreSilver) },
                            placeholder = { Text("Tell us what you think...", color = JatreSilver.copy(alpha = 0.4f)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 5,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor      = JatreGold,
                                unfocusedBorderColor    = CardBorder,
                                focusedTextColor        = JatreWhite,
                                unfocusedTextColor      = JatreWhite,
                                cursorColor             = JatreGold,
                                focusedContainerColor   = JatreBlueDark,
                                unfocusedContainerColor = JatreBlueDark
                            )
                        )

                        Spacer(Modifier.height(20.dp))

                        // Star rating
                        Text("Rate your experience", color = JatreSilver, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            (1..5).forEach { star ->
                                Icon(
                                    imageVector = if (star <= rating) Icons.Filled.Star
                                    else Icons.Filled.StarBorder,
                                    contentDescription = "Star $star",
                                    tint = if (star <= rating) JatreGold else JatreSilver,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable { rating = star }
                                )
                            }
                        }

                        // Error
                        if (errorMsg.isNotEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            Text(errorMsg, color = JatreError, fontSize = 13.sp)
                        }

                        Spacer(Modifier.height(24.dp))

                        // Submit button
                        Button(
                            onClick = {
                                if (message.isBlank()) {
                                    errorMsg = "Please write something before submitting."
                                    return@Button
                                }
                                if (rating == 0) {
                                    errorMsg = "Please select a star rating."
                                    return@Button
                                }
                                errorMsg = ""
                                isLoading = true
                                scope.launch {
                                    try {
                                        FeedbackRepository.submitFeedback(name, message, rating)
                                        isLoading = false
                                        submitted = true
                                    } catch (e: Exception) {
                                        isLoading = false
                                        errorMsg = "Failed to submit. Please try again."
                                    }
                                }
                            },
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = JatreGold)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = JatreBlueDark,
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Submit Feedback",
                                    color = JatreBlueDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}