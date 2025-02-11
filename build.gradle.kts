val ktor_version: String by project
val koin_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
    id("io.ktor.plugin") version "3.0.3"
    id("org.openapi.generator") version "7.11.0"
    id("com.google.devtools.ksp") version "2.1.10-1.0.29"
}

group = "fr.lfavreli.ranking"
version = "1.0.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Koin dependencies
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("io.insert-koin:koin-annotations:1.4.0")
    ksp("io.insert-koin:koin-ksp-compiler:1.4.0")

    // Ktor dependencies
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // DynamoDB SDK
    implementation("software.amazon.awssdk:dynamodb:2.30.15")

    implementation("org.openapitools:openapi-generator-gradle-plugin:7.11.0")

    // Test dependencies
    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

openApiGenerate {
    generatorName.set("kotlin-server")
    inputSpec.set("$rootDir/specs/ranking-api.openapi.yml")
    outputDir.set("${layout.buildDirectory.get()}/generated")
    apiPackage.set("oas.ranking.api")
    modelPackage.set("oas.ranking.model")
    configOptions.set(mapOf(
        "library" to "ktor2",
    ))
}
