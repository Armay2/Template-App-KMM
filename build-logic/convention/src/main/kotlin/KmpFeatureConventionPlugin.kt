import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Applique kmp-library + dépendances minimales qu'une feature attend.
 */
class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("template.kmp.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                getByName("commonMain").dependencies {
                    implementation(project(":shared"))
                    implementation(libs.findLibrary("kotlin-coroutines-core").get())
                    implementation(libs.findLibrary("kotlin-serialization-json").get())
                    implementation(libs.findLibrary("koin-core").get())
                    implementation(libs.findLibrary("koin-core-viewmodel").get())
                    implementation(libs.findLibrary("kermit").get())
                    implementation(libs.findLibrary("jb-lifecycle-viewmodel").get())
                }
                getByName("commonTest").dependencies {
                    implementation(kotlin("test"))
                    implementation(libs.findLibrary("kotlin-coroutines-test").get())
                    implementation(libs.findLibrary("turbine").get())
                }
            }
        }
    }
}
