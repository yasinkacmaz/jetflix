import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import io.gitlab.arturbosch.detekt.Detekt

buildscript {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap/")
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Dependencies.Gradle.androidBuildPlugin)
        classpath(Dependencies.Gradle.hiltPlugin)
        classpath(Dependencies.Gradle.kotlinPlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}

plugins {
    id(Dependencies.Gradle.Ktlint.plugin).version(Dependencies.Gradle.Ktlint.pluginVersion)
    id(Dependencies.Gradle.Detekt.plugin).version(Dependencies.Gradle.Detekt.pluginVersion)
}

ktlint {
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.HTML)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

detekt {
    buildUponDefaultConfig = true
    config = files("$projectDir/config/detekt.yml")
    baseline = file("$projectDir/config/baseline.xml")

    reports {
        html.enabled = true
        xml.enabled = true
    }
}

tasks {
    withType<Detekt> {
        this.jvmTarget = "1.8"
    }
}
