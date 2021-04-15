import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    kotlin("plugin.serialization") version "1.3.61"
}
// workaround for https://youtrack.jetbrains.com/issue/KT-27170
configurations {
    create("compileClasspath")
}
//group = "com.dimaslanjaka.facebook"
//version = "1.0.0"

repositories {
    google()
    jcenter()
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://kotlin.bintray.com/kotlinx")
}

kotlin {
    js {
        browser {
        }
        nodejs {
        }
    }
    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    // For ARM, should be changed to iosArm32 or iosArm64
    // For Linux, should be changed to e.g. linuxX64
    // For MacOS, should be changed to e.g. macosX64
    // For Windows, should be changed to e.g. mingwX64
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation ("com.soywiz.korlibs.klock:klock:2.0.0-alpha")
                //implementation(kotlin("kotlinx-coroutines-core-common"))
                //implementation(kotlin("kotlinx-serialization-runtime-common"))
                implementation("io.ktor:ktor-client-core:${rootProject.properties["ktor_version"]}")
                implementation("io.ktor:ktor-client-json:${rootProject.properties["ktor_version"]}")
                implementation("io.ktor:ktor-client-serialization:${rootProject.properties["ktor_version"]}")
                implementation("io.ktor:ktor-client-ios:${rootProject.properties["ktor_version"]}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))
            }
        }
        val androidTest by getting {
            dependsOn(androidMain)
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.1")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(kotlin("kotlinx-coroutines-core-native"))
                implementation(kotlin("kotlinx-serialization-runtime-native"))
                implementation("io.ktor:ktor-client-ios:${rootProject.properties["ktor_version"]}")
                implementation("io.ktor:ktor-client-json-native:${rootProject.properties["ktor_version"]}")
                implementation("io.ktor:ktor-client-serialization-native:${rootProject.properties["ktor_version"]}")
            }
        }
        val iosTest by getting
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosX64Test by getting { dependsOn(iosTest) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosArm64Test by getting { dependsOn(iosTest) }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

/// ANDROID configuration
android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = rootProject.version.toString()
    }
    signingConfigs {
        getByName("debug") {}
        create("release") {}
    }
    buildTypes {
        getByName("release") {
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            renderscriptOptimLevel = 0
            isMinifyEnabled = false
            multiDexEnabled = true
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            multiDexEnabled = true
            isDebuggable = true
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            renderscriptOptimLevel = 3
            isMinifyEnabled = false
            versionNameSuffix = "SNAPSHOT"
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets["test"].apply {
        resources.srcDirs("src/androidTest/resources")
        assets.srcDirs("src/androidTest/assets")
    }
}

// IOS Configuration
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
            kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)