plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("com.google.gms.google-services") version "4.4.2" apply false
}

android {
    namespace = "com.google.mapi.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.google.mapi.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
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
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.gson)
    implementation(libs.androidx.appcompat)
    debugImplementation(libs.compose.ui.tooling)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}