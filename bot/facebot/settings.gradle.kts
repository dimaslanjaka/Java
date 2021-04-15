import java.util.*

enableFeaturePreview("GRADLE_METADATA")
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                useModule("com.android.tools.build:gradle:${gradle.rootProject.properties["gradle_version"]}")
            }
        }
    }
}

rootProject.name = "Facebot"
include(":androidApp")
include(":shared")
