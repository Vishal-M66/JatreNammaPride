package com.jatrenamma.data

import java.util.Calendar

// ─── Event ───────────────────────────────────────────────
enum class EventCategory { RELIGIOUS, CULTURAL, SPORTS }

data class JatreEvent(
    val id: String,
    val name: String,
    val time: String,
    val location: String,
    val category: EventCategory,
    val isOngoing: Boolean,
    val icon: String
)

// ─── Lost & Found ─────────────────────────────────────────
data class LostFoundPost(
    val id: String,
    val description: String,
    val contact: String,
    val imageUrl: String,
    val isResolved: Boolean,
    val timestamp: String,
    val lastSeen: String = ""
)

// ─── Cultural Story ───────────────────────────────────────
data class CulturalStory(
    val id: String,
    val title: String,
    val subtitle: String,
    val body: String,
    val emoji: String
)

// ─── Nav Screen ───────────────────────────────────────────
enum class Screen { LOGIN, HOME, SCHEDULE, LOST_FOUND, MAP, SAFETY, STORIES }

// ─── Top-level helpers (OUTSIDE MockData object) ──────────
private fun eventTime(hour: Int, minute: Int = 0): String {
    val h    = if (hour % 12 == 0) 12 else hour % 12
    val m    = minute.toString().padStart(2, '0')
    val ampm = if (hour < 12) "AM" else "PM"
    return "$h:$m $ampm"
}

private fun isHappeningNow(
    startHour: Int, startMin: Int = 0,
    endHour: Int,   endMin: Int   = 0
): Boolean {
    val cal   = Calendar.getInstance()
    val now   = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    val start = startHour * 60 + startMin
    val end   = endHour   * 60 + endMin
    return now in start until end
}

// ─── Mock Data ────────────────────────────────────────────
object MockData {

    val events = listOf(
        JatreEvent("1",  "Suprabhatam & Morning Aarti", eventTime(6),      "Temple Sanctum",    EventCategory.RELIGIOUS, isHappeningNow(6,  0,  7,  0),  "🪔"),
        JatreEvent("2",  "Floral Decoration Ceremony",  eventTime(7),      "Temple Entrance",   EventCategory.RELIGIOUS, isHappeningNow(7,  0,  8,  0),  "🌸"),
        JatreEvent("3",  "Cattle Fair & Exhibition",    eventTime(8),      "Entry Gate",        EventCategory.SPORTS,    isHappeningNow(8,  0, 11,  0),  "🐄"),
        JatreEvent("4",  "Traditional Cooking Contest", eventTime(9),      "Community Kitchen", EventCategory.CULTURAL,  isHappeningNow(9,  0, 11,  0),  "🍲"),
        JatreEvent("5",  "Children's Folk Games",       eventTime(10),     "Open Grounds",      EventCategory.SPORTS,    isHappeningNow(10, 0, 12,  0),  "🪀"),
        JatreEvent("6",  "Midday Mahapuja",             eventTime(12),     "Main Temple Hall",  EventCategory.RELIGIOUS, isHappeningNow(12, 0, 13,  0),  "🛕"),
        JatreEvent("7",  "Classical Carnatic Concert",  eventTime(13),     "Auditorium",        EventCategory.CULTURAL,  isHappeningNow(13, 0, 15,  0),  "🎶"),
        JatreEvent("8",  "Kabaddi Tournament",          eventTime(14),     "Arena Ground",      EventCategory.SPORTS,    isHappeningNow(14, 0, 16, 30),  "🏅"),
        JatreEvent("9",  "Handicraft & Textile Mela",  eventTime(15),     "Exhibition Hall",   EventCategory.CULTURAL,  isHappeningNow(15, 0, 18,  0),  "🧵"),
        JatreEvent("10", "Rathotsava Procession",       eventTime(16),     "Main Road",         EventCategory.RELIGIOUS, isHappeningNow(16, 0, 18,  0),  "🛕"),
        JatreEvent("11", "Wrestling Tournament",        eventTime(18),     "Arena Ground",      EventCategory.SPORTS,    isHappeningNow(18, 0, 20,  0),  "🤼"),
        JatreEvent("12", "Yakshagana Performance",      eventTime(19),     "Open-Air Stage",    EventCategory.CULTURAL,  isHappeningNow(19, 0, 21,  0),  "🎨"),
        JatreEvent("13", "Folk Drama (Bailata)",        eventTime(20),     "Main Stage",        EventCategory.CULTURAL,  isHappeningNow(20, 0, 23,  0),  "🎭"),
        JatreEvent("14", "Night Aarti & Prasad",        eventTime(22, 30), "Temple Sanctum",    EventCategory.RELIGIOUS, isHappeningNow(22, 30, 23, 30), "🪔"),
    )

    val posts = listOf(
        LostFoundPost("1", "Young boy, red shirt, 8 years old", "9876543210", "", false, "2 hours ago"),
        LostFoundPost("2", "Black purse near food stalls",       "9845001234", "", true,  "5 hours ago"),
        LostFoundPost("3", "Elderly woman in green saree",       "9900112233", "", false, "1 hour ago"),
    )

    val stories = listOf(
        CulturalStory(
            "1", "The Legend of the Jatre", "How it all began",
            "The Jatre celebrates the divine victory of the village deity, Mallamma, " +
                    "who is believed to have protected the village from a great drought centuries ago. " +
                    "Every year, thousands gather to honour her with a grand procession, folk dances, " +
                    "and traditional games — keeping the oral tradition alive across generations.",
            "🌟"
        ),
        CulturalStory(
            "2", "Rathotsava — The Chariot Festival", "Pulling the sacred rath",
            "The Rathotsava is the centrepiece of every Jatre. A towering wooden rath " +
                    "decorated with marigolds and silk is pulled by devotees through the main road. " +
                    "It is believed that pulling the rath even once brings blessings for the entire family " +
                    "for the coming year.",
            "🛕"
        ),
        CulturalStory(
            "3", "Bailata — Folk Drama Tradition", "Stories told through dance",
            "Bailata is Karnataka's ancient folk theatre form performed under open skies. " +
                    "Stories from the Mahabharata and local legends come to life through elaborate " +
                    "costumes, drumbeats, and improvised dialogue. Performers practice for months " +
                    "to perfect their roles for the Jatre audience.",
            "🎭"
        ),
    )
}