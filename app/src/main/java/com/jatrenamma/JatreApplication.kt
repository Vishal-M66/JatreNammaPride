package com.jatrenamma

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class JatreApplication : Application() {

    companion object {
        const val DUMMY_KEY = "DUMMY_API_KEY_FOR_DEMO_MODE"
        var isDemoMode = true // Default to true for safety
            private set
    }

    override fun onCreate() {
        super.onCreate()
        
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                Log.d("JatreApp", "Initializing Firebase with dummy values for Demo Mode")
                val options = FirebaseOptions.Builder()
                    .setApiKey(DUMMY_KEY)
                    .setApplicationId("1:1234567890:android:abcdef123456")
                    .setProjectId("dummy-project-id")
                    .build()
                FirebaseApp.initializeApp(this, options)
                isDemoMode = true
            } else {
                val currentKey = FirebaseApp.getInstance().options.apiKey
                // If it's initialized but using our dummy key or no key, it's Demo Mode
                isDemoMode = (currentKey == DUMMY_KEY || currentKey.isNullOrEmpty() || currentKey == "dummy_key")
                Log.d("JatreApp", "Firebase already initialized. Demo Mode: $isDemoMode")
            }
        } catch (e: Exception) {
            Log.e("JatreApp", "Firebase init failed, forcing Demo Mode: ${e.message}")
            isDemoMode = true
        }
    }
}
