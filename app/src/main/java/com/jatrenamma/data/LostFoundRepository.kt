package com.jatrenamma.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

object LostFoundRepository {

    private val db = FirebaseFirestore.getInstance()

    // ── Save post to Firestore ────────────────────────────────────────────
    suspend fun savePost(
        description: String,
        contact: String,
        lastSeen: String
    ) {
        val post = hashMapOf(
            "description" to description,
            "contact"     to contact,
            "lastSeen"    to lastSeen,
            "imageUrl"    to "",
            "isResolved"  to false,
            "timestamp"   to com.google.firebase.Timestamp.now()  // ← real server time
        )
        db.collection("lostfound")
            .add(post)
            .await()
    }

    // ── Format Firestore timestamp into human-readable relative time ───────
    private fun formatTimestamp(doc: com.google.firebase.firestore.DocumentSnapshot): String {
        return try {
            val ts = doc.getTimestamp("timestamp")
            if (ts != null) {
                val now     = System.currentTimeMillis()
                val postMs  = ts.toDate().time
                val diffMin = (now - postMs) / 60_000
                when {
                    diffMin < 1    -> "Just now"
                    diffMin < 60   -> "${diffMin}m ago"
                    diffMin < 1440 -> "${diffMin / 60}h ago"
                    diffMin < 2880 -> "Yesterday"
                    else           -> "${diffMin / 1440}d ago"
                }
            } else {
                "Unknown time"
            }
        } catch (e: Exception) {
            "Unknown time"
        }
    }

    // ── Load all posts from Firestore ─────────────────────────────────────
    suspend fun getPosts(): List<LostFoundPost> {
        return try {
            db.collection("lostfound")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    try {
                        LostFoundPost(
                            id          = doc.id,
                            description = doc.getString("description") ?: return@mapNotNull null,
                            contact     = doc.getString("contact")     ?: return@mapNotNull null,
                            imageUrl    = "",
                            isResolved  = doc.getBoolean("isResolved") ?: false,
                            timestamp   = formatTimestamp(doc),   // ← real relative time
                            lastSeen    = doc.getString("lastSeen") ?: ""
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
        } catch (e: Exception) {
            MockData.posts // ← fallback if offline
        }
    }

    // ── Mark post as resolved ─────────────────────────────────────────────
    suspend fun markResolved(postId: String) {
        db.collection("lostfound")
            .document(postId)
            .update("isResolved", true)
            .await()
    }
}