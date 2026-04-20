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

// Modules are included as they are created in later tasks:
// - `:shared` added in Task 1.1
// - `:androidApp` added in Task 3.1
