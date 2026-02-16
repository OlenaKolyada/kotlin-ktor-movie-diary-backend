plugins {
    id("build-kmp")
}

group = rootProject.group
version = rootProject.version

//dependencies {
//    commonMainImplementation(kotlin("stdlib"))
//    commonMainImplementation(projects.entryApiKmp)
//    commonMainImplementation(projects.entryCommon)
//
//    commonTestImplementation(kotlin("test"))
//    commonTestImplementation(projects.entryStubs)
//}
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(projects.entryApiKmp)
                implementation(projects.entryCommon)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
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