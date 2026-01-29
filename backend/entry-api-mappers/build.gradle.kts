plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.entryApi)
    implementation(projects.entryCommon)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.entryStubs)
}
