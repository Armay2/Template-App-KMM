plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.skie) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

subprojects {
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
    apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom(rootProject.file("detekt.yml"))
        buildUponDefaultConfig = true
        allRules = false
        autoCorrect = false
        ignoreFailures = false
    }
}

detekt {
    config.setFrom(rootProject.file("detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
    autoCorrect = false
    ignoreFailures = false
}
