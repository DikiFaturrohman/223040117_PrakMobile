// app/build.gradle.kts (Module level)

plugins {
    // Terapkan plugin yang dideklarasikan di level proyek
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

// Konfigurasi Room KSP (letakkan di luar blok android)


android {
    namespace = "com.example.mykisah" // Pastikan namespace unik
    compileSdk = 34 // Gunakan SDK stabil terbaru (35 masih preview saat ini)

    defaultConfig {
        applicationId = "com.example.mykisah"
        minSdk = 24
        targetSdk = 34 // Sesuaikan dengan compileSdk yang stabil
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Set true untuk build rilis sesungguhnya
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Tambahkan konfigurasi penandatanganan (signing config) untuk rilis
        }
        // debug {} // Konfigurasi debug jika perlu
    }

    compileOptions {
        // Java 11 OK, tapi Java 1.8 masih umum dan didukung luas
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        freeCompilerArgs += listOf(
            "-P", "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$buildDir/compose_reports",
            "-P", "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$buildDir/compose_metrics"
        )
        // Sesuaikan dengan compileOptions
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        // buildConfig = true // aktifkan jika perlu akses BuildConfig
    }

    composeOptions {
        // Gunakan versi dari libs.versions.toml
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging { // Ganti packagingOptions dengan packaging (AGP lebih baru)
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    // Gunakan BOM Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended) // Gunakan alias dari TOML

    // Navigation
    implementation(libs.androidx.navigation.compose) // Gunakan alias dari TOML

    // Lifecycle & ViewModel & LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.compose.runtime.livedata) // Versi dari BOM
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Tambahkan jika belum ada

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // Gunakan KSP untuk Room

    // UUID
    implementation(libs.benasher44.uuid)

    // Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler) // Gunakan KSP untuk Hilt
    implementation(libs.androidx.hilt.navigation.compose) // Hilt Navigation Compose

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Gunakan BOM Compose untuk testing juga
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}