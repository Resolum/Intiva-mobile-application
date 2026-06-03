import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    // Hilt plugin for dependency injection
    alias(libs.plugins.hilt)

    // KSP plugin for annotation processing
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)

    // Firebase plugin for using Firebase services in the app
    id("com.google.gms.google-services")
}

android {
    namespace = "com.resolum.intiva"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.resolum.intiva"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/api/v1/\"")
        }
        create("docker") {
            initWith(getByName("debug"))

            buildConfigField(
                "String",
                "BASE_URL",
                "\"http://10.0.2.2/api/v1/\""
            )

            matchingFallbacks += listOf("debug")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://stocksip-back-end.azurewebsites.net/api/v1/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
}

dependencies {
    // Core dependencies
    implementation(libs.androidx.core.ktx)

    // Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.animation)

    // Retrofit and Gson Converter
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Compose Navigation dependency
    implementation(libs.androidx.navigation.compose)

    // Coil dependency for image loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // View Model dependency for usage of view models in the MVVM pattern
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Hilt dependencies for dependency injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room dependencies for local data storage
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Camara X dependencies for camera functionality
    implementation(libs.androidx.camera.camera2)
    // CameraX View dependencies for using CameraX's view-based APIs, such as PreviewView
    implementation(libs.androidx.camera.view)
    // CameraX lifecycle dependencies for managing camera lifecycle
    implementation(libs.androidx.camera.lifecycle)
    // CameraX ML Kit Vision dependencies for integrating ML Kit's vision APIs with CameraX
    implementation(libs.androidx.camera.mlkit.vision)

    // Datastore dependencies for local data storage
    implementation(libs.androidx.datastore.preferences)

    // Material Icons dependency for using Material Design icons in the app
    implementation(libs.androidx.compose.material.icons.extended)

    // Firebase dependencies for using Firebase services in the app
    implementation(platform(libs.firebase.bom))

    // Firebase Cloud Messaging dependency for handling push notifications
    implementation(libs.firebase.messaging)
}