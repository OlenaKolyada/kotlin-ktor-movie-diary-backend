plugins {
    id("build-jvm")
    application
    alias(libs.plugins.shadowJar)
}

application {
    mainClass.set("com.funkycorgi.vulpecula.entry.app.rabbit.ApplicationKt")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(libs.rabbitmq.client)
    implementation(libs.jackson.kotlin)
    implementation(libs.logback)
    implementation(libs.coroutines.core)

    implementation(projects.entryCommon)
    implementation(projects.entryAppCommon)
    implementation(projects.entryApiJvm)
    implementation(projects.entryApiJvmMappers)
    implementation(projects.entryBiz)
    implementation(projects.entryStubs)

    testImplementation(libs.testcontainers.rabbitmq)
    testImplementation(kotlin("test-junit"))
}
