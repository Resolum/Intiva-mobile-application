// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Hilt plugin for dependency injection
    alias(libs.plugins.hilt) apply false

    // KSP plugin for annotation processing
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Google Services plugin for Firebase
    id("com.google.gms.google-services") version "4.4.4" apply false

    // Firebase App Distribution plugin for Firebase App Distribution
    id("com.google.firebase.appdistribution") version "5.2.1" apply false
}