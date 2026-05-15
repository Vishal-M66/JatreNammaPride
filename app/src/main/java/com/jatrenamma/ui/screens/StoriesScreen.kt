package com.jatrenamma.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jatrenamma.data.MockData
import com.jatrenamma.ui.components.*
import com.jatrenamma.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// ─── Metadata Helper ─────────────────────────────────────────────────────────

/**
 * Fetches the API key from AndroidManifest instead of BuildConfig to avoid
 * generation errors.
 */
fun getGeminiKeyFromManifest(context: Context): String {
    return try {
        val ai = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        val key = ai.metaData.getString("com.jatrenamma.GEMINI_KEY")
        key ?: ""
    } catch (e: Exception) {
        Log.e("MANIFEST_KEY_ERROR", "Could not find GEMINI_KEY in Manifest")
        ""
    }
}

// ─── Gemini Config ───────────────────────────────────────────────────────────

private val LEGEND_PROMPT = """
    You are "The Legend" — a warm, knowledgeable, and friendly festival guide for the Jatre.
    Respond in the same language the user uses (Kannada or English).
    Keep answers to 2-3 sentences max.
    Start every response with a relevant emoji (🎡, ✨, 🙏, 🎭).
    Base your knowledge on traditional Karnataka culture and the Mallamma Jatre.
""".trimIndent()

suspend fun askTheLegend(question: String, apiKey: String): String = withContext(Dispatchers.IO) {
    if (apiKey.isBlank()) return@withContext "🙏 Error: API Key missing. Check local.properties!"

    try {
        val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=$apiKey")

        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true

        val body = JSONObject().apply {
            val partsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("text", "$LEGEND_PROMPT\n\nQuestion: $question")
                })
            }

            val contentsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", partsArray)
                })
            }

            put("contents", contentsArray)
        }

        conn.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }

        val responseCode = conn.responseCode
        if (responseCode == 200) {
            val response = conn.inputStream.bufferedReader().readText()
            val jsonResponse = JSONObject(response)

            jsonResponse.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
        } else {
            val error = conn.errorStream?.bufferedReader()?.readText()
            Log.e("GEMINI_ERROR", "Code: $responseCode | Msg: $error")
            "🙏 Error $responseCode: The Legend is silent."
        }
    } catch (e: Exception) {
        Log.e("GEMINI_EXCEPTION", e.localizedMessage ?: "Error")
        "🙏 Connection failed: ${e.localizedMessage}"
    }
}

@Composable
fun StoriesScreen() {
    val context = LocalContext.current
    val apiKey = remember { getGeminiKeyFromManifest(context) }

    var askText by remember { mutableStateOf("") }
    var aiAnswer by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(JatreBlueDark)
            .imePadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item { SectionTitle("Cultural Stories", "📖") }

                itemsIndexed(MockData.stories) { _, story ->
                    JatreCard(borderColor = JatreGold.copy(alpha = 0.4f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(listOf(JatreBlue, JatreBlueLight)),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(story.emoji, fontSize = 30.sp)
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(story.title, color = JatreGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(story.subtitle, color = JatreSilver, fontSize = 12.sp)
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(story.body, color = JatreSilverLight, fontSize = 14.sp, lineHeight = 20.sp)
                    }
                }

                if (aiAnswer.isNotEmpty() || isLoading) {
                    item {
                        SectionTitle("The Legend's Wisdom", "✨")
                        JatreCard(borderColor = JatreGold) {
                            if (isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = JatreGold,
                                        strokeWidth = 2.dp
                                    )
                                }
                            } else {
                                Text(aiAnswer, color = JatreWhite, fontSize = 14.sp, lineHeight = 21.sp)
                            }
                        }
                    }
                }
            }

            Surface(
                color = JatreBlue,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = askText,
                        onValueChange = { askText = it },
                        placeholder = { Text("Ask the Legend...", color = JatreSilver.copy(alpha = 0.5f)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = JatreWhite,
                            unfocusedTextColor = JatreWhite,
                            focusedContainerColor = JatreBlueDark,
                            unfocusedContainerColor = JatreBlueDark,
                            focusedBorderColor = JatreGold,
                            unfocusedBorderColor = CardBorder
                        )
                    )

                    Spacer(Modifier.width(12.dp))

                    IconButton(
                        onClick = {
                            if (askText.isNotBlank() && !isLoading) {
                                val question = askText
                                askText = ""
                                isLoading = true
                                aiAnswer = ""
                                scope.launch {
                                    // PASSING THE API KEY HERE
                                    aiAnswer = askTheLegend(question, apiKey)
                                    isLoading = false
                                    delay(100)
                                    if (listState.layoutInfo.totalItemsCount > 0) {
                                        listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(JatreGold, CircleShape)
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "Send", tint = JatreBlueDark)
                    }
                }
            }
        }
    }
}