package com.jatrenamma.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FeedbackRepository {

    private val db   = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun submitFeedback(name: String, message: String, rating: Int) {
        val entry = hashMapOf(
            "name"      to name.ifBlank { "Anonymous" },
            "message"   to message,
            "rating"    to rating,
            "userId"    to (auth.currentUser?.uid ?: "guest"),
            "email"     to (auth.currentUser?.email ?: "guest"),
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        // Saves to Firestore: feedback → auto-generated ID → entry
        db.collection("feedback")
            .add(entry)
            .await()
    }
}