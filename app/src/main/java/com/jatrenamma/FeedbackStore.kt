package com.jatrenamma
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FeedbackStore {

    private const val FILE_NAME = "jatre_feedback.json"

    suspend fun save(context: Context, name: String, message: String, rating: Int) {
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, FILE_NAME)

            // Load existing
            val array = if (file.exists()) JSONArray(file.readText()) else JSONArray()

            // Append new entry
            val entry = JSONObject().apply {
                put("id",      UUID.randomUUID().toString())
                put("name",    name)
                put("message", message)
                put("rating",  rating)
                put("time",    SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    .format(Date()))
            }
            array.put(entry)
            file.writeText(array.toString())

            // ── BACKEND SWAP POINT ──────────────────────────────────────
            // When ready, replace the file logic above with:
            // ApiService.submitFeedback(name, message, rating)
            // ───────────────────────────────────────────────────────────
        }
    }
}