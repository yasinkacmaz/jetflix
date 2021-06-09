import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlin-eap/")
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Dependencies.Gradle.androidBuildPlugin)
        classpath(Dependencies.Gradle.hiltPlugin)
        classpath(Dependencies.Gradle.kotlinPlugin)
        classpath(Dependencies.Gradle.kotlinSerializationPlugin)
    }
}

plugins {
    id(Dependencies.Gradle.VersionsPlugin.id) version Dependencies.Gradle.VersionsPlugin.version
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    apply(from = rootProject.file("ktlint.gradle.kts"))
    afterEvaluate {
        tasks.withType(KotlinCompile::class).all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
                allWarningsAsErrors = true
                useIR = true
                freeCompilerArgs = listOf(*freeCompilerArgs.toTypedArray(), "-Xopt-in=kotlin.RequiresOptIn")
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
    reports.html.isEnabled = false
    reports.junitXml.isEnabled = true
    maxParallelForks = Runtime.getRuntime().availableProcessors()
}

// Change gradleVersion and run gradlew wrapper to properly update gradle wrapper
tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "7.0.2"
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
