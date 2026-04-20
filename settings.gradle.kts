rootProject.name = "Template-App-KMM"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":shared")

// Remaining modules are added in later tasks:
// - `:androidApp` added in Task 3.1
