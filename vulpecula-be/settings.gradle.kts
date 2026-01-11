rootProject.name = "vulpecula-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":vulpecula-entry-api-v1-jackson")
include(":vulpecula-entry-common")
include(":vulpecula-entry-stubs")
include(":vulpecula-entry-api-v1-mappers")

//include(":vulpecula-movie-api-v1-kmp")
//include(":vulpecula-movie-common")
//include(":vulpecula-movie-stubs")
//include(":vulpecula-movie-api-v1-mappers")


