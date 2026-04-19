plugins {
    alias(libs.plugins.kotlin.jvm) apply false
//    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "com.funkycorgi.vulpecula"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

//subprojects {
//    group = rootProject.group
//    version = rootProject.version
//}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-entry", specDir.file("specs-entry.yaml").toString())
}

tasks {
    register("build") {
        group = "build"
    }

    register("check") {
        group = "verification"
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    afterEvaluate {
        if (plugins.hasPlugin("java") || plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            the<JavaPluginExtension>().apply {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(23))
                }
            }

            tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = "23"
                }
            }
        }
    }
}
