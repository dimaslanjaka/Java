plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `java`
    `groovy`
}
repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    gradlePluginPortal()
    maven("https://plugins.gradle.org/m2/")
}
dependencies {
    implementation("org.jetbrains.kotlinx:binary-compatibility-validator:latest.release")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:latest.release")
}

gradlePlugin {
    plugins.register("mpp-configuration") {
        id = "mpp-configuration"
        implementationClass = "com.example.MppConfigurationPlugin"
    }
    plugins.register("class-loader-plugin") {
        id = "class-loader-plugin"
        implementationClass = "com.example.ClassLoaderPlugin"
    }
}