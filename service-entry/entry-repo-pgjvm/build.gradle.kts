plugins {
    id("build-jvm")
}
repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(projects.entryCommon)
    api(projects.entryRepoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.uuid)

    implementation(libs.db.postgres)
    implementation(libs.bundles.exposed)
    implementation(libs.db.exposed.java.time)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.entryRepoTests)
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.logback)
}
