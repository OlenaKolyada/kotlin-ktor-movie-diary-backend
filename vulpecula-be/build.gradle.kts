plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "com.funkycorgi.vulpecula"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-entry-v1", specDir.file("specs-entry-v1.yaml").toString())
    set("spec-movie-v1", specDir.file("specs-movie-v1.yaml").toString())
}

tasks {
    register("build" ) {
        group = "build"
    }
    register("check" ) {
        group = "verification"
        subprojects.forEach { proj ->
            println("PROJ $proj")
            proj.getTasksByName("check", false).also {
                this@register.dependsOn(it)
            }
        }
    }
}
