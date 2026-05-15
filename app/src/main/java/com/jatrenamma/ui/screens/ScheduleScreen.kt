package com.jatrenamma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jatrenamma.data.EventCategory
import com.jatrenamma.data.JatreEvent
import com.jatrenamma.data.MockData
import com.jatrenamma.ui.components.*
import com.jatrenamma.ui.theme.*

@Composable
fun ScheduleScreen(isOffline: Boolean = false) {
    var selectedCategory by remember { mutableStateOf<EventCategory?>(null) }

    val filtered = if (selectedCategory == null) MockData.events
    else MockData.events.filter { it.category == selectedCategory }

    Column(modifier = Modifier.fillMaxSize().padding(vertical = 12.dp)) {
        if (isOffline) { OfflineBanner(); Spacer(Modifier.height(8.dp)) }

        SectionTitle("Event Schedule", "📅")

        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip("All",       null,                    selectedCategory, JatreSilver) { selectedCategory = null }
            FilterChip("Religious", EventCategory.RELIGIOUS, selectedCategory, JatreGold)  { selectedCategory = EventCategory.RELIGIOUS }
            FilterChip("Cultural",  EventCategory.CULTURAL,  selectedCategory, JatreSilver){ selectedCategory = EventCategory.CULTURAL }
            FilterChip("Sports",    EventCategory.SPORTS,    selectedCategory, JatreOngoing){ selectedCategory = EventCategory.SPORTS }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filtered) { event -> EventCard(event) }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    category: EventCategory?,
    selected: EventCategory?,
    activeColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    val isActive = selected == category
    Box(
        modifier = Modifier
            .background(
                if (isActive) activeColor.copy(alpha = 0.2f) else CardSurface,
                RoundedCornerShape(20.dp)
            )
            .border(1.dp, if (isActive) activeColor else CardBorder, RoundedCornerShape(20.dp))
            .padding(0.dp)
    ) {
        TextButton(onClick = onClick) {
            Text(label, color = if (isActive) activeColor else JatreSilver, fontSize = 13.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Composable
private fun EventCard(event: JatreEvent) {
    val borderColor = if (event.isOngoing) JatreOngoing else CardBorder
    JatreCard(borderColor = borderColor) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Time column
            Column(
                modifier = Modifier.width(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(event.icon, fontSize = 28.sp)
                Spacer(Modifier.height(2.dp))
                Text(event.time.split(":")[0], color = JatreGold, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                // ✅ Fixed: just use the AM/PM part directly from the time string
                Text(
                    event.time.split(" ").getOrElse(1) { "" },
                    color = JatreSilver,
                    fontSize = 11.sp
                )
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(60.dp)
                    .background(
                        if (event.isOngoing) JatreOngoing else CardBorder,
                        RoundedCornerShape(1.dp)
                    )
                    .padding(horizontal = 12.dp)
            )

            Spacer(Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(event.name, color = JatreWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, null, tint = JatreSilver, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(event.location, color = JatreSilver, fontSize = 13.sp)
                }
                Spacer(Modifier.height(6.dp))
                val catColor = when (event.category) {
                    EventCategory.RELIGIOUS -> JatreGold
                    EventCategory.CULTURAL  -> JatreSilver
                    EventCategory.SPORTS    -> JatreOngoing
                }
                CategoryBadge(event.category.name, catColor)
            }

            // Ongoing indicator
            if (event.isOngoing) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(10.dp).background(JatreOngoing, RoundedCornerShape(50)))
                    Spacer(Modifier.height(3.dp))
                    Text("LIVE", color = JatreOngoing, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
    }
}