plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.vulpeculaEntryApiV1Jackson)
    implementation(projects.vulpeculaEntryCommon)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.vulpeculaEntryStubs)
}
