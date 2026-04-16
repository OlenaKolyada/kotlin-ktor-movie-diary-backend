plugins {
    id("build-jvm")
    application
}

// TODO: ktor plugin (io.ktor.plugin) несовместим с Gradle 8 из-за Shadow 7.x.
//  Для Docker/shadowJar нужно либо обновить Ktor до 3.x, либо добавить
//  com.google.cloud.tools.jib плагин напрямую.

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        force("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.ktor:ktor-server-core:${libs.versions.ktor.get()}")
    implementation("io.ktor:ktor-server-cio:${libs.versions.ktor.get()}")
    implementation("io.ktor:ktor-server-call-logging:${libs.versions.ktor.get()}")
    implementation("io.ktor:ktor-server-cors:${libs.versions.ktor.get()}")
    implementation("io.ktor:ktor-server-content-negotiation:${libs.versions.ktor.get()}")
    implementation("io.ktor:ktor-server-default-headers:${libs.versions.ktor.get()}")
    implementation("io.ktor:ktor-server-auto-head-response:${libs.versions.ktor.get()}")
    implementation("io.ktor:ktor-server-caching-headers:${libs.versions.ktor.get()}")
    implementation("io.ktor:ktor-serialization-jackson:${libs.versions.ktor.get()}")
    implementation("io.ktor:ktor-server-config-yaml:${libs.versions.ktor.get()}")

    implementation(libs.jackson.kotlin)
    implementation(libs.logback)

    implementation(projects.entryCommon)
    implementation(projects.entryBiz)
    implementation(projects.entryAppCommon)
    implementation(projects.entryApiJvm)
    implementation(projects.entryApiJvmMappers)
    implementation(projects.entryStubs)

    testImplementation(kotlin("test-junit"))
    testImplementation("io.ktor:ktor-server-test-host:${libs.versions.ktor.get()}")
    testImplementation("io.ktor:ktor-client-content-negotiation:${libs.versions.ktor.get()}")
}
