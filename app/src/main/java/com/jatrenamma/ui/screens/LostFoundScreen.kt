package com.jatrenamma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jatrenamma.data.LostFoundPost
import com.jatrenamma.data.LostFoundRepository
import com.jatrenamma.ui.components.*
import com.jatrenamma.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LostFoundScreen(isOffline: Boolean = false) {
    val scope = rememberCoroutineScope()

    var showForm     by remember { mutableStateOf(false) }
    var posts        by remember { mutableStateOf<List<LostFoundPost>>(emptyList()) }
    var isLoading    by remember { mutableStateOf(true) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Form state
    var description by remember { mutableStateOf("") }
    var contact     by remember { mutableStateOf("") }
    var lastSeen    by remember { mutableStateOf("") }
    var itemType    by remember { mutableStateOf("Lost") } // Lost or Found
    var formError   by remember { mutableStateOf("") }

    // Load posts on start
    LaunchedEffect(Unit) {
        isLoading = true
        posts = LostFoundRepository.getPosts()
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
    ) {
        if (isOffline) {
            OfflineBanner()
            Spacer(Modifier.height(8.dp))
        }

        // ── Header ───────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle("Lost & Found", "🔍")
            if (!isOffline) {
                FloatingActionButton(
                    onClick = { showForm = !showForm },
                    containerColor = JatreGold,
                    contentColor = JatreBlueDark,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        if (showForm) Icons.Filled.Close else Icons.Filled.Add,
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // ── Post form ────────────────────────────────────────────────
            if (showForm && !isOffline) {
                item {
                    JatreCard(borderColor = JatreGold) {
                        Text(
                            "Post a Lost / Found Item",
                            color = JatreGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(Modifier.height(12.dp))

                        // ── Lost / Found toggle ──────────────────────────
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(JatreBlueDark, RoundedCornerShape(12.dp))
                                .padding(4.dp)
                        ) {
                            listOf("Lost", "Found").forEach { type ->
                                val selected = itemType == type
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            if (selected) JatreGold else JatreBlueDark,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TextButton(
                                        onClick = { itemType = type },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            if (type == "Lost") "🔍 Lost" else "✅ Found",
                                            color = if (selected) JatreBlueDark else JatreSilver,
                                            fontWeight = if (selected) FontWeight.Bold
                                            else FontWeight.Normal,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Description
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = {
                                Text(
                                    if (itemType == "Lost")
                                        "What was lost? (person/item)"
                                    else
                                        "What was found? (person/item)",
                                    color = JatreSilver
                                )
                            },
                            placeholder = {
                                Text(
                                    if (itemType == "Lost")
                                        "e.g. Young boy, red shirt, 8 years old"
                                    else
                                        "e.g. Black purse found near food stalls",
                                    color = JatreSilver.copy(alpha = 0.4f)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            shape = RoundedCornerShape(10.dp),
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

                        Spacer(Modifier.height(8.dp))

                        // Last seen location
                        OutlinedTextField(
                            value = lastSeen,
                            onValueChange = { lastSeen = it },
                            label = { Text("Last seen / Found at", color = JatreSilver) },
                            placeholder = {
                                Text(
                                    "e.g. Near main stage, Entry gate",
                                    color = JatreSilver.copy(alpha = 0.4f)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    tint = JatreGold
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
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

                        Spacer(Modifier.height(8.dp))

                        // Contact
                        OutlinedTextField(
                            value = contact,
                            onValueChange = { if (it.length <= 10) contact = it },
                            label = { Text("Contact Number (10 digits)", color = JatreSilver) },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Phone,
                                    contentDescription = null,
                                    tint = JatreGold
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
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

                        if (formError.isNotEmpty()) {
                            Spacer(Modifier.height(4.dp))
                            Text(formError, color = JatreError, fontSize = 12.sp)
                        }

                        Spacer(Modifier.height(12.dp))

                        // Submit button
                        Button(
                            onClick = {
                                formError = when {
                                    description.length < 10 ->
                                        "Description must be at least 10 characters."
                                    lastSeen.isBlank() ->
                                        "Please enter last seen / found location."
                                    contact.length != 10 ->
                                        "Enter a valid 10-digit contact number."
                                    else -> ""
                                }
                                if (formError.isEmpty()) {
                                    isSubmitting = true
                                    scope.launch {
                                        try {
                                            LostFoundRepository.savePost(
                                                description = "$itemType: $description",
                                                contact     = contact,
                                                lastSeen    = lastSeen
                                            )
                                            posts       = LostFoundRepository.getPosts()
                                            description = ""
                                            contact     = ""
                                            lastSeen    = ""
                                            showForm    = false
                                        } catch (e: Exception) {
                                            formError = "Failed to post. Please try again."
                                        } finally {
                                            isSubmitting = false
                                        }
                                    }
                                }
                            },
                            enabled = !isSubmitting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = JatreGold
                            )
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(
                                    color = JatreBlueDark,
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Post Item",
                                    color = JatreBlueDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }

            // ── Loading ──────────────────────────────────────────────────
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = JatreGold)
                    }
                }
            } else if (posts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("No posts yet", color = JatreSilver, fontSize = 16.sp)
                            Text(
                                "Tap + to report a lost or found item",
                                color = JatreSilver.copy(alpha = 0.6f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                items(posts, key = { it.id }) { post ->
                    LostFoundCard(
                        post = post,
                        onResolve = {
                            scope.launch {
                                try {
                                    LostFoundRepository.markResolved(post.id)
                                    posts = LostFoundRepository.getPosts()
                                } catch (e: Exception) { }
                            }
                        }
                    )
                }
            }
        }
    }
}

// ── Lost Found Card ───────────────────────────────────────────────────────────
@Composable
private fun LostFoundCard(post: LostFoundPost, onResolve: () -> Unit) {
    val isLost      = post.description.startsWith("Lost:")
    val borderColor = if (post.isResolved) JatreResolved else JatreUnresolved
    val typeColor   = if (isLost) JatreUnresolved else JatreResolved
    val typeLabel   = if (isLost) "🔍 Lost" else "✅ Found"

    JatreCard(borderColor = borderColor) {
        Row(verticalAlignment = Alignment.Top) {

            // Icon box
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(JatreBlueDark, RoundedCornerShape(12.dp))
                    .border(1.dp, typeColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (isLost) "🔍" else "✅", fontSize = 24.sp)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Type badge
                Box(
                    modifier = Modifier
                        .background(typeColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(typeLabel, color = typeColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    // Remove "Lost: " or "Found: " prefix for display
                    post.description.removePrefix("Lost: ").removePrefix("Found: "),
                    color = JatreWhite,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(4.dp))

                // Last seen
                if (post.lastSeen.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.LocationOn, null,
                            tint = JatreGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(post.lastSeen, color = JatreSilver, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(2.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Phone, null,
                        tint = JatreSilver,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(post.contact, color = JatreSilver, fontSize = 13.sp)
                }

                Spacer(Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Schedule, null,
                        tint = JatreSilver,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(post.timestamp, color = JatreSilver, fontSize = 12.sp)
                }
            }

            // Status badge
            Box(
                modifier = Modifier
                    .background(borderColor.copy(alpha = 0.18f), RoundedCornerShape(20.dp))
                    .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    if (post.isResolved) "✓ Resolved" else "● Open",
                    color = borderColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (!post.isResolved) {
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onResolve,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JatreResolved.copy(alpha = 0.15f),
                    contentColor = JatreResolved
                )
            ) {
                Icon(Icons.Filled.CheckCircle, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Mark as Resolved", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}