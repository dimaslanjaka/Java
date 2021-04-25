plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

repositories {
    google()
    jcenter()
    mavenLocal()
    mavenCentral()
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    composeOptions {
        kotlinCompilerVersion = "1.4.32"
        kotlinCompilerExtensionVersion = "1.0.0-alpha10"
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    ndkVersion = "22.0.7026061"
}

dependencies {
    implementation(project(":common"))
}