// Define variables for project configurations
val projectGroup = "dev.onelenyk"
val appId = "ktorhello"
val mainAppClassName = "$projectGroup.$appId.app.ApplicationKt"

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

val projectVersion: String by project

plugins {
    application // Apply the application plugin to add support for building a CLI application in Java.
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.3"

    kotlin("plugin.serialization") version "2.0.0-RC1"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("org.jetbrains.dokka") version "1.9.10"
}

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation(kotlin("stdlib"))

    // Ktor dependencies
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")

    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-request-validation:$ktorVersion")

    // Koin
    implementation("io.insert-koin:koin-core:3.4.3")
    implementation("io.insert-koin:koin-ktor:3.4.3")
    implementation("io.insert-koin:koin-logger-slf4j:3.4.3")

    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    // Test dependencies

    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClass.set(mainAppClassName)
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to mainAppClassName
            )
        )
    }

    // Include all dependencies in the JAR
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
}

tasks.shadowJar {
    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = project.version
        attributes["Main-Class"] = mainAppClassName
    }
}

// dokka

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    outputDirectory.set(buildDir.resolve("dokka"))
}
// Custom task to copy Dokka output to resources
tasks.register<Copy>("copyDokkaToResources") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    into("src/main/resources/dokka")
}

// Preparation task that generates Dokka documentation and copies it to resources
tasks.register("prepare") {
    dependsOn(tasks.dokkaHtml)
    dependsOn("copyDokkaToResources")
}

tasks.getByName("build") {
    dependsOn("prepare")
}

// Main run task that includes preparation
tasks.getByName("run") {
    dependsOn("prepare")
}

// Ensure Dokka runs during build and copies to resources
tasks.named("processResources") {
    dependsOn("copyDokkaToResources")
}

tasks {
    create("stage").dependsOn("installDist")
}

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}
