plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(projects.entryCommon)
                implementation(projects.entryStubs)
                implementation(projects.entryLibCor)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.coroutines.test)
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(23))
    })
}
