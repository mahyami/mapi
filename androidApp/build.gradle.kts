import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)

}

val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
apikeyProperties.load(FileInputStream(apikeyPropertiesFile))

android {
    namespace = "com.google.mapi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.google.mapi.android"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            type = "String",
            name = "GOOGLE_API_KEY",
            value = apikeyProperties.getProperty("GOOGLE_API_KEY")
        )
        buildConfigField(
            type = "String",
            name = "GOOGLE_GEN_AI_KEY",
            value = apikeyProperties.getProperty("GOOGLE_GEN_AI_KEY")
        )
        buildConfigField(
            type = "String",
            name = "OAUTH_CLIENT_ID",
            value = apikeyProperties.getProperty("OAUTH_CLIENT_ID")
        )
        buildConfigField(
            type = "String",
            name = "OAUTH_CLIENT_SECRET",
            value = apikeyProperties.getProperty("OAUTH_CLIENT_SECRET")
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
    signingConfigs {
        signingConfigs {
            getByName("debug") {
                keyAlias = "androiddebugkey"
                keyPassword = "android"
                storeFile = file(System.getenv("HOME") + "/.android/debug.keystore")
                storePassword = "android"
            }
        }
    }
}

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.gson)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.genai)

    implementation(libs.ktor.client.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.kotlin.test)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.logging)
    debugImplementation(libs.compose.ui.tooling)
}
