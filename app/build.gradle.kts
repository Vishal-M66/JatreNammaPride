import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // id("com.google.gms.google-services") // Commented out to fix "google-services.json is missing" error
}

android {
    namespace = "com.jatrenamma"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jatrenamma"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ─── STABLE KEY LOADING LOGIC ───
        val localProperties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(FileInputStream(localPropertiesFile))
        }

        // We use placeholders to inject keys into the AndroidManifest.
        // This is 100% stable and won't cause "Red Text" in your Kotlin code.
        manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY") ?: ""
        manifestPlaceholders["GEMINI_API_KEY"] = localProperties.getProperty("GEMINI_API_KEY") ?: ""
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        // We can keep this true, but we are moving away from using it for the API key
        // to avoid those annoying "Cannot find symbol" errors.
        buildConfig = true
    }
}

dependencies {
    // AppCompat - Fixes the "Theme.AppCompat not found" error
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.activity.compose)

    // UI & WindowInsets
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")

    // Lifecycle & ViewModel
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // Google Maps
    implementation("com.google.maps.android:maps-compose:4.3.3")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}