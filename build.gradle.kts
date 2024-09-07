import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.dependencyVersions) apply true
}

subprojects {
    apply(from = "../ktlint.gradle.kts")
    afterEvaluate {
        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
                freeCompilerArgs.add("-Xcontext-receivers")
            }
        }

        extensions.findByType<BaseExtension>() ?: return@afterEvaluate
        configure<BaseExtension> {
            compileSdkVersion(libs.versions.compileSdk.get().toInt())
            defaultConfig {
                minSdk = libs.versions.minSdk.get().toInt()
                targetSdk = libs.versions.targetSdk.get().toInt()
                versionName = libs.versions.versionName.get()
                versionCode = libs.versions.versionCode.get().toInt()
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
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
    gradleVersion = "8.5"
}
