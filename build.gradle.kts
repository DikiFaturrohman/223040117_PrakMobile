plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false // Gunakan alias dari libs.versions.toml
    alias(libs.plugins.hilt.android) apply false
    // id("org.jetbrains.kotlin.android") <- sudah benar dihapus karena redundan
}

