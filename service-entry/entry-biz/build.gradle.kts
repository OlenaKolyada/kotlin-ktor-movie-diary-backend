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
            }
        }
    }
}
