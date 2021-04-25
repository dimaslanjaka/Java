plugins {
    id("groovy")
    id("java")
    kotlin("jvm") //version "1.4.32"
    id("maven-publish")
}

description = "Facebook Library"
group = "com.dimaslanjaka.facebook"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    google()
}

@Suppress("GradleDependency") dependencies {
    // Local
    implementation(project(":library"))

    // Annotation
    implementation("org.jetbrains:annotations:20.1.0")

    // Groovy
    implementation("org.codehaus.groovy:groovy-all:3.0.8")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.32")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.32")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.32")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.32")

    // test
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // apache
    implementation("org.apache.commons:commons-lang3:3.12.0")

    // JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.json:json:20210307")

    // html parser
    implementation("org.jsoup:jsoup:1.13.1")

    // Time Unit
    implementation("joda-time:joda-time:2.10.9")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

// TODO: Jar options
tasks.withType<Jar> {
    manifest {
        attributes["Multi-Release"] = "true"
        attributes["Implementation-Title"] = project.description as String
        attributes["Implementation-Version"] = project.version as String
        attributes["Implementation-Vendor"] = "Dimas Lanjaka"
        attributes["Created-By"] =
            "${System.getProperty("java.version")} (${System.getProperty("java.vendor")} ${System.getProperty("java.vm.version")})"
        attributes["Main-Class"] = "com.dimaslanjaka.libs.facebook.Main"
    }
}

// TODO: Javadoc options
tasks.withType<Javadoc> {
    source = sourceSets.main.get().allJava
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
    (options as StandardJavadocDocletOptions).apply {
        tags = listOf("attr:a:head", "url:a", "example:code", "usage:code")
        encoding = "UTF-8"
        addStringOption("Xdoclint:none", "-quiet")
    }
}

val jar: Jar by tasks
jar.doLast {
    val jarnoversion = File(jar.archiveFile.get().asFile.parent, "facebook.jar")
    jar.archiveFile.get().asFile.copyTo(jarnoversion, true)
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.dimaslanjaka"
            artifactId = "facebook"
            version = project.version as String

            from(components["java"])
        }
    }
}

// TODO: Fix duplicate class for android project
//val libraryJar = File("$projectDir/../library/build/libs")
//libraryJar.listFiles()?.forEach { jar.from(zipTree(it)) { include("**"); exclude("META-INF", "META-INF/**"); } }

