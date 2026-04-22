plugins {
    id("template.kmp.library")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.skie)
}

kotlin {
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>().configureEach {
            export(project(":core"))
            export(projects.feature.todo)
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core"))
            api(projects.feature.todo)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlin.coroutines.test)
            implementation(libs.turbine)
        }
    }
}

android {
    namespace = "com.electra.template.shared"
}
