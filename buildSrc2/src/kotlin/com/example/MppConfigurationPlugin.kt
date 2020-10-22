package com.example;

class MppConfigurationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // Every plugin can register extensions like "kotlin" or "android"
        target.extensions.create("configuration", MppConfigurationExtension::class.java, target)
        // Invoke common function to apply the same configuration as before
        setupMultiplatformLibrary(target)
    }

    private fun setupMultiplatformLibrary(target: Project) {
        // project.apply plugin: 'org.jetbrains.kotlin.multiplatform'
        target.apply(plugin = "org.jetbrains.kotlin.multiplatform")
        // project.kotlin {
        target.extensions.configure(KotlinMultiplatformExtension::class.java) {
            sourceSets {
                maybeCreate("commonMain").dependencies { implementation(Deps.kotlin.stdlib.common) }
                maybeCreate("commonTest").dependencies {
                    implementation(Deps.kotlin.test.common)
                    implementation(Deps.kotlin.test.annotationsCommon)
                }
            }
        }
    }
}