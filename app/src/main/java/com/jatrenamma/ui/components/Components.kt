package com.jatrenamma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jatrenamma.data.Screen
import com.jatrenamma.ui.theme.*

// ─── Top Header ───────────────────────────────────────────────────────────────
@Composable
fun JatreHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(JatreBlueDark, JatreBlue))
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "🪔  ಜಾತ್ರೆ",
                style = MaterialTheme.typography.headlineMedium,
                color = JatreGold,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "NAMMA PRIDE  •  Digital Jatre Guide",
                style = MaterialTheme.typography.labelMedium,
                color = JatreSilver,
                letterSpacing = 1.5.sp
            )
        }
    }
}

// ─── Bottom Navigation ────────────────────────────────────────────────────────
data class NavItem(val screen: Screen, val icon: ImageVector, val label: String)

@Composable
fun JatreBottomNav(current: Screen, onNavigate: (Screen) -> Unit) {
    val items = listOf(
        NavItem(Screen.HOME,       Icons.Filled.Home,       "Home"),
        NavItem(Screen.SCHEDULE,   Icons.Filled.Schedule,   "Schedule"),
        NavItem(Screen.LOST_FOUND, Icons.Filled.Search,     "Lost & Found"),
        NavItem(Screen.MAP,        Icons.Filled.Map,        "Map"),
        NavItem(Screen.SAFETY,     Icons.Filled.Shield,     "Safety"),
        NavItem(Screen.STORIES,    Icons.Filled.AutoStories,"Stories"),
    )
    NavigationBar(
        containerColor = CardSurface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = current == item.screen,
                onClick  = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (current == item.screen) NavSelected else NavUnselected
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        color = if (current == item.screen) NavSelected else NavUnselected
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = JatreBlue
                )
            )
        }
    }
}

// ─── Festive Card ─────────────────────────────────────────────────────────────
@Composable
fun JatreCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CardBorder,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

// ─── Category Badge ───────────────────────────────────────────────────────────
@Composable
fun CategoryBadge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.18f), RoundedCornerShape(20.dp))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 11.sp, color = color, fontWeight = FontWeight.SemiBold)
    }
}

// ─── Gold Button ──────────────────────────────────────────────────────────────
@Composable
fun JatreButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = JatreGold,
    contentColor: Color = JatreBlueDark
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor   = contentColor
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

// ─── Section Title ────────────────────────────────────────────────────────────
@Composable
fun SectionTitle(text: String, emoji: String = "") {
    Text(
        text = if (emoji.isNotEmpty()) "$emoji  $text" else text,
        style = MaterialTheme.typography.titleLarge,
        color = JatreGold,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// ─── Divider ──────────────────────────────────────────────────────────────────
@Composable
fun JatreDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = CardBorder,
        thickness = 1.dp
    )
}

// ─── Offline Banner ───────────────────────────────────────────────────────────
@Composable
fun OfflineBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF7F3B00), RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.WifiOff, contentDescription = null, tint = JatreGoldLight, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text("Showing saved data  •  No internet connection", color = JatreGoldLight, fontSize = 13.sp)
    }
}