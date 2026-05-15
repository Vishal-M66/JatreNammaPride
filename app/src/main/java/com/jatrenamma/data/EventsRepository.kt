package com.jatrenamma.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object EventsRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getEvents(): List<Map<String, Any>> {
        return try {
            db.collection("events")
                .get()
                .await()
                .documents
                .mapNotNull { it.data }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addEvent(
        name: String,
        icon: String,
        location: String,
        time: String,
        category: String,
        isOngoing: Boolean
    ) {
        val event = hashMapOf(
            "name"      to name,
            "icon"      to icon,
            "location"  to location,
            "time"      to time,
            "category"  to category,
            "isOngoing" to isOngoing,
            "timestamp" to com.google.firebase.Timestamp.now()
        )
        db.collection("events")
            .add(event)
            .await()
    }
}