plugins {
    id("template.kmp.feature")
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.ktor.client.mock)
        }
    }
}

android {
    namespace = "com.electra.template.feature.todo"
}
