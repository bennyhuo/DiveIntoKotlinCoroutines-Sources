import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    kotlin("js") version "1.3.61"
    kotlin("plugin.serialization") version "1.3.61"
}

group = "com.bennyhuo.kotlin.coroutine.js"
version = "1.0-SNAPSHOT"

repositories {
    maven(url="https://maven.aliyun.com/repository/public")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.3")

    implementation("io.ktor:ktor-client-js:1.2.6")
    implementation("io.ktor:ktor-client-json-js:1.2.6")
    implementation("io.ktor:ktor-client-serialization-js:1.2.6")

    testImplementation("org.jetbrains.kotlin:kotlin-test-js")
}

kotlin{

    target{
        nodejs {

        }
    }

    sourceSets {
        main {
            dependencies {
                implementation(npm("axios"))
            }
        }
    }
}

rootProject.plugins.withType<NodeJsRootPlugin> {
    (extensions[NodeJsRootExtension.EXTENSION_NAME] as NodeJsRootExtension).apply {
        versions.dukat.version = "0.0.26"
    }
}