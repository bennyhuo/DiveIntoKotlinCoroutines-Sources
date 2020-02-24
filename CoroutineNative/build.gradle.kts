plugins {
    kotlin("multiplatform") version "1.3.61"
    kotlin("plugin.serialization") version "1.3.61"
}

repositories {
    maven(url = "https://mirrors.huaweicloud.com/repository/maven/")
    maven(url = "https://maven.aliyun.com/repository/public")
    maven(url = "https://kotlin.bintray.com/kotlinx")
    mavenCentral()
}

kotlin {
    // For ARM, preset function should be changed to iosArm32() or iosArm64()
    // For Linux, preset function should be changed to e.g. linuxX64()
    // For MacOS, preset function should be changed to e.g. macosX64()

    val targets = listOf(
            mingwX64(),
            linuxX64(),
            macosX64()
    )

    configure(targets) {
        binaries {
            // Comment the next section to generate Kotlin/Native library (KLIB) instead of executable file:
            executable("App") {
                // Change to specify fully qualified name of your application's entry point:
                entryPoint = "com.bennyhuo.kotlin.main"
            }
        }

        val hello by compilations.getByName("main").cinterops.creating {
            defFile(rootProject.file("src/commonMain/nativeInterop/cinterop/hello.def"))
        }
    }

    sourceSets {
        commonMain {
            dependencies {

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.3-native-mt"){
                    isForce = true
                }
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.3-native-mt") {
                    isForce = true
                }
                implementation("io.ktor:ktor-client-curl:1.3.0")
                implementation("io.ktor:ktor-client-json-native:1.3.0")
                implementation("io.ktor:ktor-client-serialization-native:1.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.14.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.14.0")
            }
        }
    }
}

// Use the following Gradle tasks to run your application:
// :runHelloWorldAppReleaseExecutableHelloWorld - without debug symbols
// :runHelloWorldAppDebugExecutableHelloWorld - with debug symbols
