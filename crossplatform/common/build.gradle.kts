import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

val gradle_version: String by rootProject.extra
val kotlin_version: String by rootProject.extra
val compose_version: String by rootProject.extra

kotlin {
    android()
    jvm("desktop")
    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation("io.ktor:ktor-client-core:1.4.1")
            }
        }
        named("androidMain") {
            dependencies {
                api("androidx.appcompat:appcompat:1.2.0")
                api("androidx.core:core-ktx:1.3.2")
                implementation("io.ktor:ktor-client-cio:1.4.1")

                // https://developer.android.com/jetpack/compose/setup
                implementation("androidx.compose.ui:ui:$compose_version")
                // Tooling support (Previews, etc.)
                implementation("androidx.compose.ui:ui-tooling:$compose_version")
                // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
                implementation("androidx.compose.foundation:foundation:$compose_version")
                // Material Design
                implementation("androidx.compose.material:material:$compose_version")
                // Material design icons
                implementation("androidx.compose.material:material-icons-core:$compose_version")
                implementation("androidx.compose.material:material-icons-extended:$compose_version")
                // Integration with observables
                implementation("androidx.compose.runtime:runtime-livedata:$compose_version")
                implementation("androidx.compose.runtime:runtime-rxjava2:$compose_version")

                // UI Tests
                //androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
            }
        }
        named("desktopMain") {
            dependencies {
                api(compose.desktop.common)
                implementation("io.ktor:ktor-client-cio:1.4.1")
            }
        }
    }
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
        //sourceCompatibility = JavaVersion.VERSION_1_8
        //targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}
dependencies {
    //implementation("androidx.ui:ui-tooling:$compose_version")
}
