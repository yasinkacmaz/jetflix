import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.android.build.gradle.BaseExtension

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

plugins {
    alias(libs.plugins.agp) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.versions) apply true
}

subprojects {
    apply(plugin = "plugins.ktlint")
    afterEvaluate {
        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = Config.javaVersion.toString()
                allWarningsAsErrors = true
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xcontext-receivers")
                // -Pandroidx.enableComposeCompilerMetrics=true
                if (project.findProperty("composeCompilerReports") == "true") {
                    freeCompilerArgs = freeCompilerArgs + listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                                project.buildDir.absolutePath + "/compose/reports"
                    )
                }
                // -Pandroidx.enableComposeCompilerReports=true
                if (project.findProperty("composeCompilerMetrics") == "true") {
                    freeCompilerArgs = freeCompilerArgs + listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                                project.buildDir.absolutePath + "/compose/metrics"
                    )
                }
            }
        }

        extensions.findByType<BaseExtension>() ?: return@afterEvaluate
        configure<BaseExtension> {
            compileSdkVersion(Config.compileSdk)
            defaultConfig {
                minSdk = Config.minSdk
                targetSdk = Config.targetSdk
                versionName = Config.versionName
                versionCode = Config.versionCode
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            compileOptions {
                sourceCompatibility = Config.javaVersion
                targetCompatibility = Config.javaVersion
            }

            testOptions {
                unitTests.apply {
                    isIncludeAndroidResources = true
                    isReturnDefaultValues = true
                }
                animationsDisabled = true
            }
        }
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    checkForGradleUpdate = true
    gradleReleaseChannel = GradleReleaseChannel.RELEASE_CANDIDATE.id
    revision = "integration"
    outputFormatter = "plain"
    outputDir = "build/dependencyUpdates"
    reportfileName = "dependency_update_report"
}

tasks.withType<Test>().configureEach {
    reports.html.required.set(false)
    reports.junitXml.required.set(true)
    maxParallelForks = Runtime.getRuntime().availableProcessors()
}

// Change gradleVersion and run gradlew wrapper to properly update gradle wrapper
tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "8.0-rc-2"
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
