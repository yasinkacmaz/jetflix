import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel

buildscript {
    repositories {
        google()
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
    }
}

subprojects {
    apply(from = rootProject.file("ktlint.gradle.kts"))
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    checkForGradleUpdate = true
    gradleReleaseChannel = GradleReleaseChannel.RELEASE_CANDIDATE.id
    revision = "integration"
    outputFormatter = "plain,json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "dependency_update_report"
}

tasks.withType<Test>().configureEach {
    reports.html.isEnabled = false
    reports.junitXml.isEnabled = true
    maxParallelForks = Runtime.getRuntime().availableProcessors()
}

tasks.withType<JavaCompile>().configureEach {
    options.isFork = true
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
