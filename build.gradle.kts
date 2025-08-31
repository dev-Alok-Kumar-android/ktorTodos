plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.2.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

group = "com.alokkumar"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.cio.EngineMain"
}

dependencies {
    // Ktor Core and Server Engine
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-cio") // You've already chosen CIO, good.

    // Serialization (JSON conversion)
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    // Routing
    implementation("io.ktor:ktor-server-sessions") // Not required but a very useful plugin, if you want to add this, add this.
    implementation("io.ktor:ktor-server-call-logging") // Useful for logging requests.
    implementation("io.ktor:ktor-server-default-headers") // Adds useful headers like Last-Modified.
    implementation("io.ktor:ktor-server-netty") // Alternative to CIO.
    implementation("io.ktor:ktor-server-resources") // Useful for type-safe routing.
    implementation("io.ktor:ktor-server-status-pages") // For handling errors and exceptions.
    implementation("io.ktor:ktor-server-webjars") // For serving static files.
    implementation("io.ktor:ktor-server-freemarker") // For serving dynamic HTML pages.

    // Database and ORM (Exposed)
    implementation("org.postgresql:postgresql:42.7.3") // Your database driver
    implementation("org.jetbrains.exposed:exposed-core:0.51.1") // Core Exposed library
    implementation("org.jetbrains.exposed:exposed-dao:0.51.1") // Exposed's data access objects
    implementation("org.jetbrains.exposed:exposed-jdbc:0.51.1") // JDBC connection for Exposed
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.51.1") // If you use LocalDate/Instant
    implementation("com.zaxxer:HikariCP:5.1.0") // A connection pool for better performance

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("org.slf4j:slf4j-api:2.0.13")

    // Configuration
    implementation("io.ktor:ktor-server-config-yaml")

    // auth
    implementation("com.auth0:java-jwt:4.5.0")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    testImplementation("io.ktor:ktor-server-test-host-jvm:3.2.3")
}