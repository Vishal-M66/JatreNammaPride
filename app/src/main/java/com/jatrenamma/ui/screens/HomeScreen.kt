package com.jatrenamma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jatrenamma.data.EventCategory
import com.jatrenamma.data.MockData
import com.jatrenamma.data.JatreEvent
import com.jatrenamma.ui.components.*
import com.jatrenamma.ui.theme.*
import com.jatrenamma.FeedbackScreen
import kotlinx.coroutines.delay
import java.util.Calendar

// ── Time helpers ──────────────────────────────────────────────────────────────

private fun nowInMinutes(): Int {
    val cal = Calendar.getInstance()
    return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
}

private fun eventStartMinutes(timeStr: String): Int {
    return try {
        val upper  = timeStr.trim().uppercase()
        val isPM   = upper.contains("PM")
        val digits = upper.replace("AM", "").replace("PM", "").trim()
        val parts  = digits.split(":")
        var hour   = parts[0].trim().toInt()
        val min    = if (parts.size > 1) parts[1].trim().toInt() else 0
        if (isPM && hour != 12) hour += 12
        if (!isPM && hour == 12) hour = 0
        hour * 60 + min
    } catch (e: Exception) { -1 }
}

private fun isNowOngoing(event: JatreEvent): Boolean {
    val allEvents = MockData.events
    val idx   = allEvents.indexOf(event)
    val start = eventStartMinutes(event.time)
    val end   = if (idx + 1 < allEvents.size)
        eventStartMinutes(allEvents[idx + 1].time)
    else
        start + 60
    val now = nowInMinutes()
    return now in start until end
}

// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(isOffline: Boolean = false) {

    var tick by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000L)
            tick++
        }
    }

    // ✅ Fixed: eventStartMinutes now receives .time (String), not the event object
    val ongoingEvent by remember(tick) {
        derivedStateOf { MockData.events.firstOrNull { isNowOngoing(it) } }
    }
    val nextEvent by remember(tick) {
        derivedStateOf {
            val now = nowInMinutes()
            MockData.events.firstOrNull { eventStartMinutes(it.time) > now }
        }
    }

    var showFeedback by remember { mutableStateOf(false) }
    if (showFeedback) {
        FeedbackScreen(onBack = { showFeedback = false })
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isOffline) OfflineBanner()

        // ── Welcome banner ───────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(JatreBlue, JatreBlueLight)),
                    RoundedCornerShape(20.dp)
                )
                .border(1.dp, JatreGold.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    "🪔  Welcome to the Jatre!",
                    style = MaterialTheme.typography.titleLarge,
                    color = JatreGold,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Explore events, locate facilities, and stay connected.",
                    color = JatreSilver,
                    fontSize = 14.sp
                )
            }
        }

        // ── Live Now card ────────────────────────────────────────────────
        if (ongoingEvent != null) {
            JatreCard(borderColor = JatreOngoing) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(JatreOngoing, RoundedCornerShape(50))
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "LIVE NOW",
                        color = JatreOngoing,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    ongoingEvent!!.icon + "  " + ongoingEvent!!.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = JatreWhite,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = JatreSilver, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(ongoingEvent!!.location, color = JatreSilver, fontSize = 14.sp)
                    Spacer(Modifier.width(12.dp))
                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = JatreSilver, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(ongoingEvent!!.time, color = JatreSilver, fontSize = 14.sp)
                }
            }
        } else {
            JatreCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("😴", fontSize = 22.sp)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "No event happening right now.\nCheck the schedule for upcoming events.",
                        color = JatreSilver,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // ── Next Up badge ────────────────────────────────────────────────
        if (nextEvent != null) {
            JatreCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Timer, contentDescription = null, tint = JatreGold, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Next Up", color = JatreSilver, fontSize = 12.sp)
                        Text(
                            nextEvent!!.icon + "  " + nextEvent!!.name + "  ·  " + nextEvent!!.time,
                            color = JatreWhite,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // ── Quick stats ──────────────────────────────────────────────────
        SectionTitle("Today's Highlights", "📋")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatChip(Modifier.weight(1f), MockData.events.size.toString(), "Events", "🗓")
            StatChip(Modifier.weight(1f), MockData.posts.count { !it.isResolved }.toString(), "Lost", "🔍")
            StatChip(Modifier.weight(1f), "3", "Map Pins", "📍")
        }

        // ── Schedule preview ─────────────────────────────────────────────
        SectionTitle("Schedule Preview", "📅")
        MockData.events.take(5).forEach { event ->
            val happening = isNowOngoing(event)
            JatreCard(borderColor = if (happening) JatreOngoing else CardBorder) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(event.icon, fontSize = 26.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(event.name, color = JatreWhite, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Text("${event.time}  •  ${event.location}", color = JatreSilver, fontSize = 13.sp)
                    }
                    val catColor = when (event.category) {
                        EventCategory.RELIGIOUS -> JatreGold
                        EventCategory.CULTURAL  -> JatreSilver
                        EventCategory.SPORTS    -> JatreOngoing
                    }
                    CategoryBadge(event.category.name, catColor)
                }
                if (happening) {
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(JatreOngoing.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("● Happening Now", color = JatreOngoing, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ── Feedback button ──────────────────────────────────────────────
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardSurface, RoundedCornerShape(20.dp))
                .border(1.dp, JatreGold.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(4.dp)
        ) {
            TextButton(
                onClick = { showFeedback = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Feedback, contentDescription = null, tint = JatreGold, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Share Your Feedback 🪔", color = JatreGold, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun StatChip(modifier: Modifier, count: String, label: String, emoji: String) {
    Box(
        modifier = modifier
            .background(CardSurface, RoundedCornerShape(14.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 22.sp)
            Text(count, color = JatreGold, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(label, color = JatreSilver, fontSize = 11.sp)
        }
    }
}