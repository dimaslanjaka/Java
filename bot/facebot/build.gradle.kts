buildscript {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
        mavenLocal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://kotlin.bintray.com/kotlinx")

        maven("https://dl.bintray.com/kotlin/kotlinx")
        maven("https://dl.bintray.com/kotlin/ktor")
        maven("https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kodein-framework/Kodein-DI")
        maven("https://dl.bintray.com/touchlabpublic/kotlin")
        maven("https://repository.jboss.org/nexus/content/repositories/ea/")
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io")
        maven("https://repo1.maven.org/maven2/")
        maven("https://jcenter.bintray.com/")
        maven("https://maven.google.com")
        maven("https://dl.bintray.com/android/android-tools")
        maven("https://repository.jboss.org/nexus/content/repositories/releases/")
        maven("https://maven.fabric.io/public")
        maven("https://oss.sonatype.org/content/repositories/releases/")
        maven("https://repo.spring.io/plugins-release/")
        maven("https://maven.atlassian.com/content/repositories/atlassian-public/")
        maven("https://repo.spring.io/libs-milestone/")
        maven("https://repo.hortonworks.com/content/repositories/releases/")
        maven("https://repo.spring.io/libs-release/")
    }
    dependencies {
        classpath("com.dimaslanjaka:gradle-plugin:latest.release")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.properties["kotlin_version"]}")
        classpath("com.android.tools.build:gradle:4.1.0")
    }
}

//apply(plugin = "com.dimaslanjaka")

allprojects {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
        mavenLocal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://kotlin.bintray.com/kotlinx")

        maven("https://dl.bintray.com/kotlin/kotlinx")
        maven("https://dl.bintray.com/kotlin/ktor")
        maven("https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kodein-framework/Kodein-DI")
        maven("https://dl.bintray.com/touchlabpublic/kotlin")
        maven("https://repository.jboss.org/nexus/content/repositories/ea/")
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io")
        maven("https://repo1.maven.org/maven2/")
        maven("https://jcenter.bintray.com/")
        maven("https://maven.google.com")
        maven("https://dl.bintray.com/android/android-tools")
        maven("https://repository.jboss.org/nexus/content/repositories/releases/")
        maven("https://maven.fabric.io/public")
        maven("https://oss.sonatype.org/content/repositories/releases/")
        maven("https://repo.spring.io/plugins-release/")
        maven("https://maven.atlassian.com/content/repositories/atlassian-public/")
        maven("https://repo.spring.io/libs-milestone/")
        maven("https://repo.hortonworks.com/content/repositories/releases/")
        maven("https://repo.spring.io/libs-release/")
    }
}
apply(from = "android.gradle")
subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.freeCompilerArgs = listOf(
            "-Xuse-experimental=kotlinx.serialization.ImplicitReflectionSerializer",
            "-Xuse-experimental=kotlinx.serialization.UnstableDefault"
        )
        kotlinOptions {
            suppressWarnings = true
        }
    }
}