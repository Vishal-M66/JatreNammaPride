package com.jatrenamma.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.jatrenamma.JatreApplication
import com.jatrenamma.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    val scope = rememberCoroutineScope()

    var isSignUp     by remember { mutableStateOf(false) }
    var name         by remember { mutableStateOf("") }
    var email        by remember { mutableStateOf("") }
    var password     by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMsg     by remember { mutableStateOf("") }
    var isLoading    by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(JatreBlueDark, JatreBlue, JatreBlueDark))
            )
            .imePadding()
    ) {
        // Decorative glow circles
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset((-60).dp, (-60).dp)
                .background(JatreBlueLight.copy(alpha = 0.12f), androidx.compose.foundation.shape.CircleShape)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(60.dp, 60.dp)
                .background(JatreGold.copy(alpha = 0.07f), androidx.compose.foundation.shape.CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Branding
            Spacer(Modifier.height(20.dp))
            Text("🪔", fontSize = 64.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "ಜಾತ್ರೆ",
                style = MaterialTheme.typography.displayLarge,
                color = JatreGold,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "NAMMA PRIDE",
                style = MaterialTheme.typography.labelMedium,
                color = JatreSilver,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Your Digital Jatre Companion",
                style = MaterialTheme.typography.bodyMedium,
                color = JatreSilver.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            // Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    // Tab row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(JatreBlueDark, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        listOf("Sign In", "Sign Up").forEachIndexed { idx, label ->
                            val selected = (idx == 0) == !isSignUp
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (selected) JatreGold else JatreBlueDark,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(
                                    onClick = { isSignUp = (idx == 1); errorMsg = "" },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        label,
                                        color = if (selected) JatreBlueDark else JatreSilver,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    AnimatedVisibility(visible = isSignUp) {
                        Column {
                            JatreTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = "Full Name",
                                placeholder = "e.g. Rakshitha J",
                                leadingIcon = Icons.Filled.Person
                            )
                            Spacer(Modifier.height(14.dp))
                        }
                    }

                    JatreTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        placeholder = "you@example.com",
                        leadingIcon = Icons.Filled.Email,
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(Modifier.height(14.dp))

                    JatreTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        placeholder = "Min 6 characters",
                        leadingIcon = Icons.Filled.Lock,
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (showPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.VisibilityOff
                                    else Icons.Filled.Visibility,
                                    contentDescription = null,
                                    tint = JatreSilver
                                )
                            }
                        }
                    )

                    if (errorMsg.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Text(errorMsg, color = JatreError, fontSize = 13.sp)
                    }

                    Spacer(Modifier.height(22.dp))

                    Button(
                        onClick = {
                            errorMsg = validate(isSignUp, name, email, password)
                            if (errorMsg.isNotEmpty()) return@Button

                            isLoading = true
                            scope.launch {
                                try {
                                    // ─── BYPASS LOGIC ───
                                    // Check if we are in demo mode (no real firebase config)
                                    if (!JatreApplication.isDemoMode) {
                                        if (isSignUp) {
                                            auth.createUserWithEmailAndPassword(email.trim(), password).await()
                                        } else {
                                            auth.signInWithEmailAndPassword(email.trim(), password).await()
                                        }
                                    } else {
                                        // In demo mode, simulate success with a delay
                                        delay(800)
                                    }
                                    
                                    onLoginSuccess()
                                } catch (e: Exception) {
                                    errorMsg = when {
                                        e.message?.contains("email address is already") == true ->
                                            "This email is already registered. Sign in instead."
                                        e.message?.contains("no user record") == true ->
                                            "No account found. Sign up first."
                                        e.message?.contains("password is invalid") == true ->
                                            "Wrong password. Please try again."
                                        e.message?.contains("badly formatted") == true ->
                                            "Please enter a valid email address."
                                        else -> "Something went wrong. Please try again."
                                    }
                                } finally {
                                    isLoading = false
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
                                if (isSignUp) "Create Account" else "Sign In  🪔",
                                color = JatreBlueDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    TextButton(
                        onClick = onLoginSuccess,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Continue as Guest →",
                            color = JatreSilver,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "By continuing you agree to Jatre-Namma's terms of use.",
                color = JatreSilver.copy(alpha = 0.5f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun validate(signUp: Boolean, name: String, email: String, password: String): String {
    if (signUp && name.isBlank())          return "Please enter your name."
    if (email.isBlank())                   return "Please enter your email."
    if (!email.contains("@"))             return "Please enter a valid email address."
    if (password.length < 6)              return "Password must be at least 6 characters."
    return ""
}

@Composable
fun JatreTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = JatreSilver) },
        placeholder = { Text(placeholder, color = JatreSilver.copy(alpha = 0.4f)) },
        leadingIcon = leadingIcon?.let { icon ->
            { Icon(icon, contentDescription = null, tint = JatreGold) }
        },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
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
}
