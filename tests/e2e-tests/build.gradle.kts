plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(23)
}

repositories {
    maven {
        name = "LocalRepo"
        url = uri("${rootProject.projectDir}/../deploy/build/repo")
    }
}

val resourcesFromLib by configurations.creating

dependencies {
    implementation(kotlin("stdlib"))

    resourcesFromLib("com.funkycorgi.vulpecula:dcompose:1.0:resources@zip")

    implementation("com.funkycorgi.vulpecula:entry-api-jvm")
    implementation("com.funkycorgi.vulpecula:entry-api-jvm-mappers")
    implementation("com.funkycorgi.vulpecula:entry-api-kmp")
    implementation("com.funkycorgi.vulpecula:entry-stubs")

    testImplementation(kotlin("test-junit5"))

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        dependsOn("extractLibResources")
    }
    register<Copy>("extractLibResources") {
        from(zipTree(resourcesFromLib.singleFile))
        into(layout.buildDirectory.dir("dcompose"))
    }
}
