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
                    api(project(":core"))
                    // core's api(...) on coroutines/serialization/koin/kermit/jb-lifecycle-viewmodel
                    // propagates transitively — no need to re-declare here.
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
