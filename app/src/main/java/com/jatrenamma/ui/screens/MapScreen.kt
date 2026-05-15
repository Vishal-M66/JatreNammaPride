package com.jatrenamma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.jatrenamma.ui.theme.*

// ── Data models ───────────────────────────────────────────────────────────────
data class MapPin(
    val id: String,
    val title: String,
    val description: String,
    val latLng: LatLng,
    val type: PinType,
    val emoji: String
)

enum class PinType { EVENT, FOOD, TOILET, PARKING, MEDICAL }

// ── Pin data (Dharwad, Karnataka) ─────────────────────────────────────────────
val jatrePins = listOf(
    // Events
    MapPin("1", "Rathotsava Procession", "Main cultural event • 4:00 PM",
        LatLng(15.4589, 75.0078), PinType.EVENT, "🛕"),
    MapPin("2", "Wrestling Tournament", "Sports event • 6:00 PM",
        LatLng(15.4592, 75.0082), PinType.EVENT, "🤼"),
    MapPin("3", "Folk Drama (Bailata)", "Cultural show • 8:00 PM",
        LatLng(15.4585, 75.0074), PinType.EVENT, "🎭"),
    MapPin("4", "Cattle Fair", "Agricultural event • 9:00 AM",
        LatLng(15.4595, 75.0086), PinType.EVENT, "🐄"),

    // Facilities
    MapPin("5", "Food Stalls", "Local food & snacks available",
        LatLng(15.4591, 75.0080), PinType.FOOD, "🍱"),
    MapPin("6", "Toilet Block A", "Clean facilities available",
        LatLng(15.4587, 75.0076), PinType.TOILET, "🚻"),
    MapPin("7", "Toilet Block B", "Near main stage",
        LatLng(15.4593, 75.0084), PinType.TOILET, "🚻"),
    MapPin("8", "Parking Area", "Free parking available",
        LatLng(15.4582, 75.0070), PinType.PARKING, "🅿️"),
    MapPin("9", "Medical Camp", "First aid & emergency",
        LatLng(15.4597, 75.0090), PinType.MEDICAL, "🏥"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val jatreCentre = LatLng(15.4589, 75.0078)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(jatreCentre, 17f)
    }

    var selectedPin    by remember { mutableStateOf<MapPin?>(null) }
    var selectedFilter by remember { mutableStateOf<PinType?>(null) }

    val filteredPins = if (selectedFilter == null) jatrePins
    else jatrePins.filter { it.type == selectedFilter }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Google Map ────────────────────────────────────────────────────
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            )
        ) {
            filteredPins.forEach { pin ->
                Marker(
                    state = MarkerState(position = pin.latLng),
                    title = pin.title,
                    snippet = pin.description,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        when (pin.type) {
                            PinType.EVENT   -> BitmapDescriptorFactory.HUE_YELLOW
                            PinType.FOOD    -> BitmapDescriptorFactory.HUE_ORANGE
                            PinType.TOILET  -> BitmapDescriptorFactory.HUE_BLUE
                            PinType.PARKING -> BitmapDescriptorFactory.HUE_CYAN
                            PinType.MEDICAL -> BitmapDescriptorFactory.HUE_RED
                        }
                    ),
                    onClick = {
                        selectedPin = pin
                        false
                    }
                )
            }
        }

        // ── Filter chips row ──────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedFilter == null,
                onClick = { selectedFilter = null },
                label = { Text("All", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = JatreGold,
                    selectedLabelColor = JatreBlueDark
                )
            )
            listOf(
                PinType.EVENT   to "🛕 Events",
                PinType.FOOD    to "🍱 Food",
                PinType.TOILET  to "🚻 Toilets",
                PinType.PARKING to "🅿️ Park",
                PinType.MEDICAL to "🏥 Medical"
            ).forEach { (type, label) ->
                FilterChip(
                    selected = selectedFilter == type,
                    onClick = {
                        selectedFilter = if (selectedFilter == type) null else type
                    },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = JatreGold,
                        selectedLabelColor = JatreBlueDark
                    )
                )
            }
        }

        // ── Legend ────────────────────────────────────────────────────────
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 56.dp, end = 8.dp)
                .border(1.dp, CardBorder, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardSurface.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Legend",
                    color = JatreGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                LegendItem("🟡", "Events")
                LegendItem("🟠", "Food")
                LegendItem("🔵", "Toilets")
                LegendItem("🩵", "Parking")
                LegendItem("🔴", "Medical")
            }
        }

        // ── Bottom sheet when pin tapped ──────────────────────────────────
        selectedPin?.let { pin ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            JatreGold.copy(alpha = 0.5f),
                            RoundedCornerShape(20.dp)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(pin.emoji, fontSize = 32.sp)
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        pin.title,
                                        color = JatreGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        when (pin.type) {
                                            PinType.EVENT   -> "📅 Event"
                                            PinType.FOOD    -> "🍱 Food & Drinks"
                                            PinType.TOILET  -> "🚻 Facility"
                                            PinType.PARKING -> "🅿️ Parking"
                                            PinType.MEDICAL -> "🏥 Medical"
                                        },
                                        color = JatreSilver,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            IconButton(onClick = { selectedPin = null }) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = JatreSilver
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        HorizontalDivider(color = CardBorder)
                        Spacer(Modifier.height(8.dp))

                        Text(
                            pin.description,
                            color = JatreWhite,
                            fontSize = 14.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = JatreGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "%.4f, %.4f".format(
                                    pin.latLng.latitude,
                                    pin.latLng.longitude
                                ),
                                color = JatreSilver,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(dot: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(dot, fontSize = 10.sp)
        Spacer(Modifier.width(4.dp))
        Text(label, color = JatreSilver, fontSize = 10.sp)
    }
}