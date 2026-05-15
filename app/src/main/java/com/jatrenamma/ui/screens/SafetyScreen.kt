package com.jatrenamma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jatrenamma.ui.components.*
import com.jatrenamma.ui.theme.*

@Composable
fun SafetyScreen(isOffline: Boolean = false) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            if (isOffline) OfflineBanner()
            Spacer(Modifier.height(if (isOffline) 8.dp else 0.dp))
            SectionTitle("Safety Information", "🛡️")
        }

        // Emergency contacts
        item {
            JatreCard(borderColor = JatreError) {
                Text("🚨  Emergency Contacts", color = JatreError, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(Modifier.height(10.dp))
                EmergencyRow("Police Control Room",  "100", JatreError)
                JatreDivider()
                EmergencyRow("Ambulance / Medical",  "108", Color(0xFFF39C12))
                JatreDivider()
                EmergencyRow("Fire Department",      "101", Color(0xFFE67E22))
                JatreDivider()
                EmergencyRow("Jatre Help Desk",      "9845000001", JatreGold)
            }
        }

        // First-Aid location
        item {
            JatreCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏥", fontSize = 28.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("First-Aid Post", color = JatreGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Near the Main Stage entrance\nOpen 24 hours during the Jatre", color = JatreSilver, fontSize = 13.sp)
                    }
                }
            }
        }

        // Safety guidelines
        item { SectionTitle("Safety Guidelines", "📋") }

        val guidelines = listOf(
            Triple("👶", "Children", "Keep children with you at all times. Register lost children at the Help Desk near the entry gate."),
            Triple("🚗", "Parking",  "Use designated Parking Zone A or B only. Do not block emergency vehicle access routes."),
            Triple("🥤", "Hydration","Drink water regularly — free water stations are located near food stalls and First-Aid post."),
            Triple("💊", "Medical",  "If you need medicine, inform the First-Aid team. Do not share medication with others."),
            Triple("🔥", "Fire",     "No open flames or fireworks except during designated Rathotsava procession. Report smoking violations."),
            Triple("📱", "Contact",  "Save the Jatre Help Desk number. Share your location with a family member before moving around."),
        )

        items(guidelines.size) { idx ->
            val (emoji, title, body) = guidelines[idx]
            JatreCard {
                Row {
                    Text(emoji, fontSize = 24.sp, modifier = Modifier.padding(top = 2.dp))
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(title, color = JatreGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(Modifier.height(3.dp))
                        Text(body, color = JatreSilver, fontSize = 13.sp)
                    }
                }
            }
        }

        // Offline note
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(JatreBlue.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .border(1.dp, JatreBlueLight, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.OfflineBolt, null, tint = JatreGold, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("This screen works offline — cached for emergency access.", color = JatreSilver, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun EmergencyRow(label: String, number: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = JatreWhite, fontSize = 14.sp)
        Box(
            modifier = Modifier
                .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(number, color = color, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}
