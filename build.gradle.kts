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
        classpath(Dependencies.Gradle.kotlinSerializationPlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    apply(from = rootProject.file("ktlint.gradle.kts"))
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
