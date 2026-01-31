plugins {
    id("build-jvm")
    id("maven-publish")
}

group = "com.funkycorgi.vulpecula.deploy"
version = rootProject.version

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    register("buildInfra") {
        group = "build"
        dependsOn(project(":vulpecula-dcompose").getTasksByName("publish",false))
    }
}