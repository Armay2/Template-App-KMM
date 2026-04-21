plugins {
    id("template.kmp.library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.coroutines.core)
            api(libs.kotlin.serialization.json)
            api(libs.kotlin.datetime)
            api(libs.ktor.client.core)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.client.logging)
            api(libs.ktor.serialization.json)
            api(libs.koin.core)
            api(libs.koin.core.viewmodel)
            api(libs.kermit)
            api(libs.multiplatform.settings)
            api(libs.multiplatform.settings.coroutines)
            api(libs.jb.lifecycle.viewmodel)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlin.coroutines.test)
            implementation(libs.turbine)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.electra.template.core"
}
