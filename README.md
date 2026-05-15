# JatreNammaPride 🎡 ✨

**JatreNammaPride** is a modern Android application designed to serve as a digital bridge between Karnataka's rich traditional village fairs (Jatre) and today's mobile-first generation. This project was developed as part of my 8th-semester engineering curriculum and internship.

---

## 🎭 The Legend (AI Guide)
The standout feature of this app is **"The Legend"**, a warm and knowledgeable AI festival guide powered by the **Google Gemini API**. It provides:
* **Cultural Insights:** Answers questions about local traditions and history.
* **Bilingual Support:** Responds in both English and Kannada.
* **Concise Wisdom:** Delivers context-aware, friendly advice in 2-3 sentences.

---

## 🛠️ Tech Stack
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Declarative UI)
* **AI Engine:** Google Gemini Flash (v1beta)
* **Architecture:** MVVM (Model-View-ViewModel)
* **Networking:** HttpURLConnection with JSON parsing
* **Graphics:** Custom theming with Gold, Silver, and Deep Blue palettes

---

## 🛡️ Security & Architecture
This project follows professional security standards for API key management:
* **Zero-Footprint:** API keys are stored in `local.properties` and are **not** tracked by Git.
* **Manifest Injection:** Keys are passed through Gradle Manifest Placeholders for maximum stability and to avoid `BuildConfig` generation errors.

---

## 📖 Key Features
- **Cultural Stories:** A curated scrollable list of local folklore and festival history.
- **Interactive Chat:** A custom-built chat interface for interacting with the AI guide.
- **Themed UI:** A "Royal Dark" aesthetic inspired by traditional festival colors.

---

## 🚀 Getting Started
To run this project locally:🚀 Getting Started
To run this project locally, you must provide your own API credentials as they are secured and not included in this repository.
Create a file named local.properties in the root directory of the project and add the following lines:
Properties
GEMINI_API_KEY=your_gemini_api_key_here
MAPS_API_KEY=your_google_maps_key_here
Google Cloud Setup
For AI: Go to Google AI Studio and generate a Gemini API key.
For Maps: Go to the Google Cloud Console, enable the Maps SDK for Android, and generate an API key.
🗺️ Map Configuration Note
For the Google Maps to render correctly:
Ensure your MAPS_API_KEY has the Maps SDK for Android enabled in the Google Cloud Console.
If you are testing on a real device, ensure location permissions are granted when prompted by the app.
Build and Run via Android Studio

---
*Created by Rakshitha Jayashankar*
