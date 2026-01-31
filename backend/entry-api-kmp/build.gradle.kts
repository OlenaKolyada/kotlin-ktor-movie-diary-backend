import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("build-kmp")
    alias(libs.plugins.crowdproj.generator)
    alias(libs.plugins.kotlinx.serialization)
}

crowdprojGenerate {
    packageName.set("${project.group}.entry.api.kmp")
    inputSpec.set(rootProject.ext["spec-entry"] as String)
}

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/src/commonMain/kotlin"))
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(projects.entryCommon)

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(projects.entryStubs)
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks {
    val openApiGenerateTask: GenerateTask = getByName("openApiGenerate", GenerateTask::class) {
        outputDir.set(layout.buildDirectory.file("generate-resources").get().toString())
        finalizedBy("compileCommonMainKotlinMetadata")
    }
    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerateTask)
    }
}
