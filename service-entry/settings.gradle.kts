rootProject.name = "service-entry"

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
//        id("build-kmp") apply false
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

include(":entry-api-jvm")
include(":entry-api-jvm-mappers")
//include(":entry-api-kmp")
//include(":entry-api-kmp-mappers")
include(":entry-biz")
include(":entry-app-common")
include(":entry-app-ktor")
include(":entry-app-rabbit")
include(":entry-common")
include(":entry-stubs")